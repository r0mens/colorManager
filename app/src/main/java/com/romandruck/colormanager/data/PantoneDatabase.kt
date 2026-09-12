package com.romandruck.colormanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
entities = [
NewPantoneColors1Entity::class,
NewPantoneColors2Entity::class,
NewPantoneColors3Entity::class,
FlexoColorPaletteEntity::class,
CustomColorPaletteEntity::class,
InkLibrary::class,
AniloxFlex::class
],
version = 10002025,
exportSchema = true
)
abstract class PantoneDatabase : RoomDatabase() {

abstract fun newPantoneColors1Dao(): NewPantoneColors1Dao

abstract fun newPantoneColors2Dao(): NewPantoneColors2Dao

abstract fun newPantoneColors3Dao(): NewPantoneColors3Dao

abstract fun flexoColorPaletteDao(): FlexoColorPaletteDao

abstract fun customColorPaletteDao(): CustomColorPaletteDao

abstract fun inkLibraryDao(): InkLibraryDao

abstract fun aniloxFlexDao(): AniloxFlexDao

companion object {

    @Volatile
    private var INSTANCE: PantoneDatabase? = null

    fun getInstance(context: Context): PantoneDatabase {

        return INSTANCE ?: synchronized(this) {

            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                PantoneDatabase::class.java,
                "pantone_colors.db"
            )
                .createFromAsset("pantone_colors.db")
                .build()
                .also {
                    INSTANCE = it
                }
        }
    }
}


}