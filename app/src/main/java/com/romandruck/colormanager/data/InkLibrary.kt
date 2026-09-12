package com.romandruck.colormanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ink_library",
    indices = [
        Index(
            value = [
                "manufacturer",
                "name",
                "ink_code"
            ],
            unique = true
        )
    ]
)
data class InkLibrary(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "manufacturer")
    val manufacturer: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "ink_code")
    val ink_code: String
)
