package com.romandruck.colormanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface InkLibraryDao {

    @Query("""
        SELECT * FROM ink_library
        ORDER BY manufacturer, name
    """)
    fun getAll(): Flow<List<InkLibrary>>


    @Query("""
        SELECT * FROM ink_library
        WHERE manufacturer LIKE '%' || :query || '%'
           OR name LIKE '%' || :query || '%'
           OR ink_code LIKE '%' || :query || '%'
        ORDER BY manufacturer, name
    """)
    fun search(query: String): Flow<List<InkLibrary>>


    @Insert
    suspend fun insert(ink: InkLibrary)


    @Update
    suspend fun update(ink: InkLibrary)


    @Delete
    suspend fun delete(ink: InkLibrary)


    @Query("""
        SELECT * FROM ink_library
        WHERE id = :id
        LIMIT 1
    """)
    suspend fun getById(id: Int): InkLibrary?
}
