package com.romandruck.colormanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "new_pantone_colors3")
data class NewPantoneColors3Entity(

    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "hex_code")
    val hexCode: String,

    @ColumnInfo(name = "base_color1")
    val baseColor1: String,

    @ColumnInfo(name = "percent_color1")
    val percentColor1: Double,

    @ColumnInfo(name = "base_color2")
    val baseColor2: String,

    @ColumnInfo(name = "percent_color2")
    val percentColor2: Double,

    @ColumnInfo(name = "base_color3")
    val baseColor3: String,

    @ColumnInfo(name = "percent_color3")
    val percentColor3: Double,

    @ColumnInfo(name = "base_color4")
    val baseColor4: String,

    @ColumnInfo(name = "percent_color4")
    val percentColor4: Double,

    @ColumnInfo(name = "description")
    val description: String?
)
