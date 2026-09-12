package com.romandruck.colormanager.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class PantoneRepository(
    private val database: PantoneDatabase
) {

    fun getAllColors(): Flow<List<PantoneItem>> {

        return combine(
            database.newPantoneColors1Dao().getAllColors(),
            database.newPantoneColors2Dao().getAllColors(),
            database.newPantoneColors3Dao().getAllColors()
        ) { colors1, colors2, colors3 ->

            buildList {

                // Таблица new_pantone_colors1
                colors1.forEach { color ->

                    add(
                        PantoneItem(
                            name = color.name,
                            hexCode = color.hexCode,

                            baseColor1 = color.baseColor1,
                            percentColor1 = color.percentColor1,

                            baseColor2 = color.baseColor2,
                            percentColor2 = color.percentColor2,

                            description = color.description
                        )
                    )
                }

                // Таблица new_pantone_colors2
                colors2.forEach { color ->

                    add(
                        PantoneItem(
                            name = color.name,
                            hexCode = color.hexCode,

                            baseColor1 = color.baseColor1,
                            percentColor1 = color.percentColor1,

                            baseColor2 = color.baseColor2,
                            percentColor2 = color.percentColor2,

                            baseColor3 = color.baseColor3,
                            percentColor3 = color.percentColor3,

                            description = color.description
                        )
                    )
                }

                // Таблица new_pantone_colors3
                colors3.forEach { color ->

                    add(
                        PantoneItem(
                            name = color.name,
                            hexCode = color.hexCode,

                            baseColor1 = color.baseColor1,
                            percentColor1 = color.percentColor1,

                            baseColor2 = color.baseColor2,
                            percentColor2 = color.percentColor2,

                            baseColor3 = color.baseColor3,
                            percentColor3 = color.percentColor3,

                            baseColor4 = color.baseColor4,
                            percentColor4 = color.percentColor4,

                            description = color.description
                        )
                    )
                }

            }.sortedBy { it.name }
        }
    }

    fun searchColors(query: String): Flow<List<PantoneItem>> {

        return combine(
            database.newPantoneColors1Dao().searchColors(query),
            database.newPantoneColors2Dao().searchColors(query),
            database.newPantoneColors3Dao().searchColors(query)
        ) { colors1, colors2, colors3 ->

            buildList {

                // Таблица new_pantone_colors1
                colors1.forEach { color ->

                    add(
                        PantoneItem(
                            name = color.name,
                            hexCode = color.hexCode,

                            baseColor1 = color.baseColor1,
                            percentColor1 = color.percentColor1,

                            baseColor2 = color.baseColor2,
                            percentColor2 = color.percentColor2,

                            description = color.description
                        )
                    )
                }

                // Таблица new_pantone_colors2
                colors2.forEach { color ->

                    add(
                        PantoneItem(
                            name = color.name,
                            hexCode = color.hexCode,

                            baseColor1 = color.baseColor1,
                            percentColor1 = color.percentColor1,

                            baseColor2 = color.baseColor2,
                            percentColor2 = color.percentColor2,

                            baseColor3 = color.baseColor3,
                            percentColor3 = color.percentColor3,

                            description = color.description
                        )
                    )
                }

                // Таблица new_pantone_colors3
                colors3.forEach { color ->

                    add(
                        PantoneItem(
                            name = color.name,
                            hexCode = color.hexCode,

                            baseColor1 = color.baseColor1,
                            percentColor1 = color.percentColor1,

                            baseColor2 = color.baseColor2,
                            percentColor2 = color.percentColor2,

                            baseColor3 = color.baseColor3,
                            percentColor3 = color.percentColor3,

                            baseColor4 = color.baseColor4,
                            percentColor4 = color.percentColor4,

                            description = color.description
                        )
                    )
                }

            }.sortedBy { it.name }
        }
    }
}
