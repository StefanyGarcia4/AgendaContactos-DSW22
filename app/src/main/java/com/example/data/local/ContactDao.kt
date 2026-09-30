package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para realizar operaciones CRUD sobre la tabla 'contacts'.
 */
@Dao
interface ContactDao {

    @Query("SELECT * FROM contacts ORDER BY isFavorite DESC, name COLLATE NOCASE ASC")
    fun getAllContacts(): Flow<List<Contact>>

    @Query("""
        SELECT * FROM contacts 
        WHERE name LIKE '%' || :query || '%' 
           OR phone LIKE '%' || :query || '%' 
           OR email LIKE '%' || :query || '%'
        ORDER BY isFavorite DESC, name COLLATE NOCASE ASC
    """)
    fun searchContacts(query: String): Flow<List<Contact>>

    @Query("""
        SELECT * FROM contacts 
        WHERE (:category = 'Todos' OR category = :category)
          AND (name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%')
        ORDER BY isFavorite DESC, name COLLATE NOCASE ASC
    """)
    fun getFilteredContacts(query: String, category: String): Flow<List<Contact>>

    @Query("SELECT * FROM contacts WHERE id = :id")
    fun getContactById(id: Long): Flow<Contact?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contacts: List<Contact>)

    @Update
    suspend fun updateContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteContactById(id: Long)

    @Query("SELECT COUNT(*) FROM contacts")
    suspend fun getCount(): Int
}
