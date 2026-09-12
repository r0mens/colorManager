package com.romandruck.colormanager.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewPantoneColors2Dao {

    @Query("SELECT * FROM new_pantone_colors2 ORDER BY name")
    fun getAllColors(): Flow<List<NewPantoneColors2Entity>>

    @Query("""
        SELECT * FROM new_pantone_colors2
        WHERE name LIKE '%' || :query || '%'
        ORDER BY name
    """)
    fun searchColors(query: String): Flow<List<NewPantoneColors2Entity>>

    @Query("SELECT * FROM new_pantone_colors2 WHERE name = :name LIMIT 1")
    suspend fun getColorByName(name: String): NewPantoneColors2Entity?
}
