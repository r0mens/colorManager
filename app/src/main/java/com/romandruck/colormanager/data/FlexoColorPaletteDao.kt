package com.romandruck.colormanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlexoColorPaletteDao {

    @Query(
        """
        SELECT * 
        FROM flexo_color_palette 
        ORDER BY name
        """
    )
    fun getAll(): Flow<List<FlexoColorPaletteEntity>>

    @Query(
        """
        SELECT *
        FROM flexo_color_palette
        WHERE name LIKE '%' || :query || '%'
        ORDER BY name
        """
    )
    fun search(
        query: String
    ): Flow<List<FlexoColorPaletteEntity>>

    @Query(
        """
        SELECT *
        FROM flexo_color_palette
        WHERE name = :name
        LIMIT 1
        """
    )
    suspend fun getByName(
        name: String
    ): FlexoColorPaletteEntity?

    @Insert
    suspend fun insert(
        recipe: FlexoColorPaletteEntity
    )

    @Update
    suspend fun update(
        recipe: FlexoColorPaletteEntity
    )

    @Delete
    suspend fun delete(
        recipe: FlexoColorPaletteEntity
    )

    @Query(
        """
        DELETE FROM flexo_color_palette
        WHERE name = :name
        """
    )
    suspend fun deleteByName(
        name: String
    )
}
