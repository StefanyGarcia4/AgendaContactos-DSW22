package com.example.data.repository

import com.example.data.local.Contact
import com.example.data.local.ContactDao
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio que abstrae el acceso a la base de datos Room para la entidad Contact.
 */
class ContactRepository(private val contactDao: ContactDao) {

    val allContacts: Flow<List<Contact>> = contactDao.getAllContacts()

    fun getFilteredContacts(query: String, category: String): Flow<List<Contact>> {
        return contactDao.getFilteredContacts(query.trim(), category)
    }

    fun getContactById(id: Long): Flow<Contact?> {
        return contactDao.getContactById(id)
    }

    suspend fun insertContact(contact: Contact): Long {
        return contactDao.insertContact(contact)
    }

    suspend fun updateContact(contact: Contact) {
        contactDao.updateContact(contact)
    }

    suspend fun deleteContact(contact: Contact) {
        contactDao.deleteContact(contact)
    }

    suspend fun deleteContactById(id: Long) {
        contactDao.deleteContactById(id)
    }

    suspend fun seedInitialDataIfEmpty() {
        if (contactDao.getCount() == 0) {
            val sampleContacts = listOf(
                Contact(
                    name = "Josué Hernández Chorro",
                    phone = "+503 7123-4567",
                    email = "josue.hernandez.chorro@gmail.com",
                    category = "Trabajo",
                    notes = "Docente - Materia Desarrollo de Aplicaciones Móviles ITCA FEPADE",
                    isFavorite = true
                ),
                Contact(
                    name = "Marcelo Santa Cruz",
                    phone = "+503 7890-1234",
                    email = "marcelosantacruz29@gmail.com",
                    category = "Personal",
                    notes = "Estudiante - Técnico en Ing. de Desarrollo de Software",
                    isFavorite = true
                ),
                Contact(
                    name = "Coordinación ITCA",
                    phone = "+503 2121-5500",
                    email = "soporte.academico@itca.edu.sv",
                    category = "Trabajo",
                    notes = "Escuela de Ingeniería en Computación - Sede Central",
                    isFavorite = false
                ),
                Contact(
                    name = "Carlos Mendoza",
                    phone = "+503 7654-3210",
                    email = "carlos.mendoza@gmail.com",
                    category = "Amigos",
                    notes = "Compañero de proyecto de desarrollo móvil",
                    isFavorite = false
                ),
                Contact(
                    name = "Dra. Elena Ramos",
                    phone = "+503 2234-5678",
                    email = "elena.ramos@medicos.com",
                    category = "Familia",
                    notes = "Médico de cabecera familiar",
                    isFavorite = false
                )
            )
            contactDao.insertAll(sampleContacts)
        }
    }
}
