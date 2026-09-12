package com.romandruck.colormanager.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class RecipeRepository(
    private val database: PantoneDatabase
) {

    /* ============================================================
       DAO
       ============================================================ */

    private val flexoDao =
        database.flexoColorPaletteDao()

    private val offsetDao =
        database.customColorPaletteDao()

    private val inkLibraryDao =
        database.inkLibraryDao()

    private val aniloxFlexDao =
        database.aniloxFlexDao()


    /* ============================================================
       INK LIBRARY
       ============================================================ */

    fun getAllInks(): Flow<List<InkLibrary>> {

        return inkLibraryDao.getAll()
    }


    fun searchInks(
        query: String
    ): Flow<List<InkLibrary>> {

        return inkLibraryDao.search(query)
    }


    suspend fun getInkById(
        id: Int
    ): InkLibrary? {

        return inkLibraryDao.getById(id)
    }


    suspend fun addInk(
        ink: InkLibrary
    ) {

        inkLibraryDao.insert(ink)
    }


    suspend fun updateInk(
        ink: InkLibrary
    ) {

        inkLibraryDao.update(ink)
    }


    suspend fun deleteInk(
        ink: InkLibrary
    ) {

        inkLibraryDao.delete(ink)
    }

    /* ============================================================
   ANILOX FLEX
   ============================================================ */

    fun getAllAnilox(): Flow<List<AniloxFlex>> {

        return aniloxFlexDao.getAll()
    }


    fun searchAnilox(
        query: String
    ): Flow<List<AniloxFlex>> {

        return aniloxFlexDao.search(query)
    }


    suspend fun addAnilox(
        anilox: AniloxFlex
    ) {

        aniloxFlexDao.insert(anilox)
    }


    suspend fun updateAnilox(
        anilox: AniloxFlex
    ) {

        aniloxFlexDao.update(anilox)
    }


    suspend fun deleteAnilox(
        anilox: AniloxFlex
    ) {

        aniloxFlexDao.delete(anilox)
    }




    /* ============================================================
       FLEXO
       ============================================================ */

    fun getAllFlexoRecipes(): Flow<List<CustomRecipe>> {

        return flexoDao
            .getAll()
            .map { entities ->

                entities.map { entity ->

                    CustomRecipe(
                        name = entity.name,
                        hexCode = entity.hexCode,

                        type = RecipeType.FLEXO,

                        baseColor1 = entity.baseColor1,
                        percentColor1 = entity.percentColor1,

                        baseColor2 = entity.baseColor2,
                        percentColor2 = entity.percentColor2,

                        baseColor3 = entity.baseColor3,
                        percentColor3 = entity.percentColor3,

                        baseColor4 = entity.baseColor4,
                        percentColor4 = entity.percentColor4,

                        anilox = entity.anilox,

                        description = entity.description
                    )
                }
            }
    }


    fun searchFlexoRecipes(
        query: String
    ): Flow<List<CustomRecipe>> {

        return flexoDao
            .search(query)
            .map { entities ->

                entities.map { entity ->

                    CustomRecipe(
                        name = entity.name,
                        hexCode = entity.hexCode,

                        type = RecipeType.FLEXO,

                        baseColor1 = entity.baseColor1,
                        percentColor1 = entity.percentColor1,

                        baseColor2 = entity.baseColor2,
                        percentColor2 = entity.percentColor2,

                        baseColor3 = entity.baseColor3,
                        percentColor3 = entity.percentColor3,

                        baseColor4 = entity.baseColor4,
                        percentColor4 = entity.percentColor4,

                        anilox = entity.anilox,

                        description = entity.description
                    )
                }
            }
    }


    suspend fun getFlexoRecipe(
        name: String
    ): CustomRecipe? {

        return flexoDao
            .getByName(name)
            ?.let { entity ->

                CustomRecipe(
                    name = entity.name,
                    hexCode = entity.hexCode,

                    type = RecipeType.FLEXO,

                    baseColor1 = entity.baseColor1,
                    percentColor1 = entity.percentColor1,

                    baseColor2 = entity.baseColor2,
                    percentColor2 = entity.percentColor2,

                    baseColor3 = entity.baseColor3,
                    percentColor3 = entity.percentColor3,

                    baseColor4 = entity.baseColor4,
                    percentColor4 = entity.percentColor4,

                    anilox = entity.anilox,

                    description = entity.description
                )
            }
    }


    suspend fun addFlexoRecipe(
        recipe: CustomRecipe
    ) {

        flexoDao.insert(

            FlexoColorPaletteEntity(

                name = recipe.name,

                hexCode = recipe.hexCode,

                baseColor1 = recipe.baseColor1,
                percentColor1 = recipe.percentColor1,

                baseColor2 = recipe.baseColor2,
                percentColor2 = recipe.percentColor2,

                baseColor3 = recipe.baseColor3,
                percentColor3 = recipe.percentColor3,

                baseColor4 = recipe.baseColor4,
                percentColor4 = recipe.percentColor4,

                anilox = recipe.anilox,

                description = recipe.description
            )
        )
    }


    suspend fun updateFlexoRecipe(
        recipe: CustomRecipe
    ) {

        flexoDao.update(

            FlexoColorPaletteEntity(

                name = recipe.name,

                hexCode = recipe.hexCode,

                baseColor1 = recipe.baseColor1,
                percentColor1 = recipe.percentColor1,

                baseColor2 = recipe.baseColor2,
                percentColor2 = recipe.percentColor2,

                baseColor3 = recipe.baseColor3,
                percentColor3 = recipe.percentColor3,

                baseColor4 = recipe.baseColor4,
                percentColor4 = recipe.percentColor4,

                anilox = recipe.anilox,

                description = recipe.description
            )
        )
    }


    suspend fun deleteFlexoRecipe(
        name: String
    ) {

        flexoDao.deleteByName(name)
    }


    /* ============================================================
       OFFSET
       ============================================================ */

    fun getAllOffsetRecipes(): Flow<List<CustomRecipe>> {

        return offsetDao
            .getAll()
            .map { entities ->

                entities.map { entity ->

                    CustomRecipe(
                        name = entity.name,
                        hexCode = entity.hexCode,

                        type = RecipeType.OFFSET,

                        baseColor1 = entity.baseColor1,
                        percentColor1 = entity.percentColor1,

                        baseColor2 = entity.baseColor2,
                        percentColor2 = entity.percentColor2,

                        baseColor3 = entity.baseColor3,
                        percentColor3 = entity.percentColor3,

                        baseColor4 = entity.baseColor4,
                        percentColor4 = entity.percentColor4,

                        description = entity.description
                    )
                }
            }
    }


    fun searchOffsetRecipes(
        query: String
    ): Flow<List<CustomRecipe>> {

        return offsetDao
            .search(query)
            .map { entities ->

                entities.map { entity ->

                    CustomRecipe(
                        name = entity.name,
                        hexCode = entity.hexCode,

                        type = RecipeType.OFFSET,

                        baseColor1 = entity.baseColor1,
                        percentColor1 = entity.percentColor1,

                        baseColor2 = entity.baseColor2,
                        percentColor2 = entity.percentColor2,

                        baseColor3 = entity.baseColor3,
                        percentColor3 = entity.percentColor3,

                        baseColor4 = entity.baseColor4,
                        percentColor4 = entity.percentColor4,

                        description = entity.description
                    )
                }
            }
    }


    suspend fun getOffsetRecipe(
        name: String
    ): CustomRecipe? {

        return offsetDao
            .getByName(name)
            ?.let { entity ->

                CustomRecipe(
                    name = entity.name,
                    hexCode = entity.hexCode,

                    type = RecipeType.OFFSET,

                    baseColor1 = entity.baseColor1,
                    percentColor1 = entity.percentColor1,

                    baseColor2 = entity.baseColor2,
                    percentColor2 = entity.percentColor2,

                    baseColor3 = entity.baseColor3,
                    percentColor3 = entity.percentColor3,

                    baseColor4 = entity.baseColor4,
                    percentColor4 = entity.percentColor4,

                    description = entity.description
                )
            }
    }


    suspend fun addOffsetRecipe(
        recipe: CustomRecipe
    ) {

        offsetDao.insert(

            CustomColorPaletteEntity(

                name = recipe.name,

                hexCode = recipe.hexCode,

                baseColor1 = recipe.baseColor1,
                percentColor1 = recipe.percentColor1,

                baseColor2 = recipe.baseColor2,
                percentColor2 = recipe.percentColor2,

                baseColor3 = recipe.baseColor3,
                percentColor3 = recipe.percentColor3,

                baseColor4 = recipe.baseColor4,
                percentColor4 = recipe.percentColor4,

                description = recipe.description
            )
        )
    }


    suspend fun updateOffsetRecipe(
        recipe: CustomRecipe
    ) {

        offsetDao.update(

            CustomColorPaletteEntity(

                name = recipe.name,

                hexCode = recipe.hexCode,

                baseColor1 = recipe.baseColor1,
                percentColor1 = recipe.percentColor1,

                baseColor2 = recipe.baseColor2,
                percentColor2 = recipe.percentColor2,

                baseColor3 = recipe.baseColor3,
                percentColor3 = recipe.percentColor3,

                baseColor4 = recipe.baseColor4,
                percentColor4 = recipe.percentColor4,

                description = recipe.description
            )
        )
    }


    suspend fun deleteOffsetRecipe(
        name: String
    ) {

        offsetDao.deleteByName(name)
    }
}
