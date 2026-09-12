package com.romandruck.colormanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomColorPaletteDao {

    @Query(
        """
        SELECT *
        FROM Castom_color_palette
        ORDER BY name
        """
    )
    fun getAll(): Flow<List<CustomColorPaletteEntity>>

    @Query(
        """
        SELECT *
        FROM Castom_color_palette
        WHERE name LIKE '%' || :query || '%'
        ORDER BY name
        """
    )
    fun search(
        query: String
    ): Flow<List<CustomColorPaletteEntity>>

    @Query(
        """
        SELECT *
        FROM Castom_color_palette
        WHERE name = :name
        LIMIT 1
        """
    )
    suspend fun getByName(
        name: String
    ): CustomColorPaletteEntity?

    @Insert
    suspend fun insert(
        recipe: CustomColorPaletteEntity
    )

    @Update
    suspend fun update(
        recipe: CustomColorPaletteEntity
    )

    @Delete
    suspend fun delete(
        recipe: CustomColorPaletteEntity
    )

    @Query(
        """
        DELETE FROM Castom_color_palette
        WHERE name = :name
        """
    )
    suspend fun deleteByName(
        name: String
    )
}
