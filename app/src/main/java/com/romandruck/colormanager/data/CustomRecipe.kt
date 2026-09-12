package com.romandruck.colormanager.data

data class CustomRecipe(

    val name: String,

    val hexCode: String,

    val type: RecipeType,

    val baseColor1: String,

    val percentColor1: Double,

    val baseColor2: String,

    val percentColor2: Double,

    val baseColor3: String? = null,

    val percentColor3: Double? = null,

    val baseColor4: String? = null,

    val percentColor4: Double? = null,

    val anilox: String? = null,

    val description: String? = null
)
