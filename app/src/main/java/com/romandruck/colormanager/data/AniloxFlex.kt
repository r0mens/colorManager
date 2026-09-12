package com.romandruck.colormanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anilox_flex")
data class AniloxFlex(

    @PrimaryKey
    val name: String,

    val volume: Double?
)
