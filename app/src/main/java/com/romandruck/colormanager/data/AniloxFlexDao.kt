package com.romandruck.colormanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AniloxFlexDao {

    @Query("""
        SELECT * FROM anilox_flex
        ORDER BY name
    """)
    fun getAll(): Flow<List<AniloxFlex>>


    @Query("""
        SELECT * FROM anilox_flex
        WHERE name LIKE '%' || :query || '%'
        ORDER BY name
    """)
    fun search(query: String): Flow<List<AniloxFlex>>


    @Insert
    suspend fun insert(anilox: AniloxFlex)


    @Update
    suspend fun update(anilox: AniloxFlex)


    @Delete
    suspend fun delete(anilox: AniloxFlex)
}
