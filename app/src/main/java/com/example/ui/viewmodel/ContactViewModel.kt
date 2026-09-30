package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.Contact
import com.example.data.repository.ContactRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado general de la UI para la pantalla de Contactos.
 */
data class ContactUiState(
    val contacts: List<Contact> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String = "Todos",
    val isFormDialogOpen: Boolean = false,
    val contactToDelete: Contact? = null,
    val contactToView: Contact? = null,
    val formState: ContactFormState = ContactFormState(),
    val snackbarMessage: String? = null,
    val isLoading: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todos")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _isFormDialogOpen = MutableStateFlow(false)
    private val _contactToDelete = MutableStateFlow<Contact?>(null)
    private val _contactToView = MutableStateFlow<Contact?>(null)
    private val _formState = MutableStateFlow(ContactFormState())
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)

    // Flujo reactivo de contactos basado en búsqueda y categoría
    private val filteredContacts = combine(_searchQuery, _selectedCategory) { query, category ->
        Pair(query, category)
    }.flatMapLatest { (query, category) ->
        repository.getFilteredContacts(query, category)
    }

    val uiState: StateFlow<ContactUiState> = combine(
        filteredContacts,
        _searchQuery,
        _selectedCategory,
        _isFormDialogOpen,
        _contactToDelete,
        _contactToView,
        _formState,
        _snackbarMessage,
        _isLoading
    ) { params: Array<Any?> ->
        @Suppress("UNCHECKED_CAST")
        ContactUiState(
            contacts = params[0] as List<Contact>,
            searchQuery = params[1] as String,
            selectedCategory = params[2] as String,
            isFormDialogOpen = params[3] as Boolean,
            contactToDelete = params[4] as? Contact,
            contactToView = params[5] as? Contact,
            formState = params[6] as ContactFormState,
            snackbarMessage = params[7] as? String,
            isLoading = params[8] as Boolean
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ContactUiState()
    )

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    // --- Búsqueda y Filtros ---
    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    // --- Manejo del Formulario (Crear / Actualizar) ---
    fun openAddContactDialog() {
        _formState.value = ContactFormState(
            isEditMode = false,
            category = if (_selectedCategory.value != "Todos") _selectedCategory.value else "Personal"
        )
        _isFormDialogOpen.value = true
    }

    fun openEditContactDialog(contact: Contact) {
        _formState.value = ContactFormState(
            id = contact.id,
            name = contact.name,
            phone = contact.phone,
            email = contact.email,
            category = contact.category,
            notes = contact.notes,
            isFavorite = contact.isFavorite,
            isEditMode = true
        )
        _isFormDialogOpen.value = true
    }

    fun closeFormDialog() {
        _isFormDialogOpen.value = false
        _formState.value = ContactFormState()
    }

    fun onNameChanged(name: String) {
        val error = validateName(name)
        _formState.update { it.copy(name = name, nameError = error) }
    }

    fun onPhoneChanged(phone: String) {
        val error = validatePhone(phone)
        _formState.update { it.copy(phone = phone, phoneError = error) }
    }

    fun onEmailChanged(email: String) {
        val error = validateEmail(email)
        _formState.update { it.copy(email = email, emailError = error) }
    }

    fun onCategoryChanged(category: String) {
        _formState.update { it.copy(category = category) }
    }

    fun onNotesChanged(notes: String) {
        _formState.update { it.copy(notes = notes) }
    }

    fun onFavoriteChanged(isFavorite: Boolean) {
        _formState.update { it.copy(isFavorite = isFavorite) }
    }

    // --- Reglas de Validación ---
    private fun validateName(name: String): String? {
        val trimmed = name.trim()
        return when {
            trimmed.isEmpty() -> "El nombre completo es obligatorio"
            trimmed.length < 3 -> "Debe contener al menos 3 caracteres"
            !trimmed.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s.'-]+$")) -> "Solo se permiten letras y espacios"
            else -> null
        }
    }

    private fun validatePhone(phone: String): String? {
        val trimmed = phone.trim()
        val digitsOnly = trimmed.filter { it.isDigit() }
        return when {
            trimmed.isEmpty() -> "El teléfono es obligatorio"
            digitsOnly.length < 8 -> "Debe ingresar al menos 8 dígitos numéricos"
            !trimmed.matches(Regex("^[+]?[0-9\\s-]{8,20}$")) -> "Formato inválido (ej: +503 7123-4567 o 71234567)"
            else -> null
        }
    }

    private fun validateEmail(email: String): String? {
        val trimmed = email.trim()
        if (trimmed.isEmpty()) return null // Email es opcional
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return if (!trimmed.matches(emailRegex)) {
            "Formato de correo inválido (ej: usuario@dominio.com)"
        } else null
    }

    /**
     * Guarda un contacto nuevo o actualiza uno existente tras validar.
     */
    fun saveContact() {
        val current = _formState.value
        val nameErr = validateName(current.name)
        val phoneErr = validatePhone(current.phone)
        val emailErr = validateEmail(current.email)

        if (nameErr != null || phoneErr != null || emailErr != null) {
            _formState.update {
                it.copy(
                    nameError = nameErr,
                    phoneError = phoneErr,
                    emailError = emailErr
                )
            }
            return
        }

        viewModelScope.launch {
            val contact = Contact(
                id = current.id,
                name = current.name.trim(),
                phone = current.phone.trim(),
                email = current.email.trim(),
                category = current.category,
                notes = current.notes.trim(),
                isFavorite = current.isFavorite
            )

            if (current.isEditMode) {
                repository.updateContact(contact)
                _snackbarMessage.value = "Contacto actualizado exitosamente"
            } else {
                repository.insertContact(contact)
                _snackbarMessage.value = "Contacto guardado exitosamente"
            }

            closeFormDialog()
        }
    }

    // --- Eliminación (Delete) ---
    fun openDeleteDialog(contact: Contact) {
        _contactToDelete.value = contact
    }

    fun closeDeleteDialog() {
        _contactToDelete.value = null
    }

    fun confirmDeleteContact() {
        val toDelete = _contactToDelete.value ?: return
        viewModelScope.launch {
            repository.deleteContact(toDelete)
            _snackbarMessage.value = "Contacto '${toDelete.name}' eliminado"
            _contactToDelete.value = null
            if (_contactToView.value?.id == toDelete.id) {
                _contactToView.value = null
            }
        }
    }

    // --- Detalle / Vista rápida ---
    fun openViewContact(contact: Contact) {
        _contactToView.value = contact
    }

    fun closeViewContact() {
        _contactToView.value = null
    }

    // --- Acciones Rápidas ---
    fun toggleFavorite(contact: Contact) {
        viewModelScope.launch {
            val updated = contact.copy(isFavorite = !contact.isFavorite)
            repository.updateContact(updated)
            // Si está viéndose en el diálogo de detalles, actualizarlo también
            if (_contactToView.value?.id == contact.id) {
                _contactToView.value = updated
            }
        }
    }

    fun dismissSnackbar() {
        _snackbarMessage.value = null
    }

    fun reloadSampleData() {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
            _snackbarMessage.value = "Datos de prueba listos"
        }
    }

    class Factory(private val repository: ContactRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ContactViewModel::class.java)) {
                return ContactViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
