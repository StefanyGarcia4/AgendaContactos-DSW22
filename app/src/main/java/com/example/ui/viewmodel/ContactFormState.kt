package com.example.ui.viewmodel

/**
 * Estado y errores de validación del formulario de Contactos.
 */
data class ContactFormState(
    val id: Long = 0,
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val category: String = "Personal",
    val notes: String = "",
    val isFavorite: Boolean = false,
    val nameError: String? = null,
    val phoneError: String? = null,
    val emailError: String? = null,
    val isEditMode: Boolean = false
) {
    val isValid: Boolean
        get() = nameError == null && phoneError == null && emailError == null &&
                name.isNotBlank() && phone.isNotBlank()
}
