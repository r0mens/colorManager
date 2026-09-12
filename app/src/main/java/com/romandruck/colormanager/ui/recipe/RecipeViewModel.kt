package com.romandruck.colormanager.ui.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.romandruck.colormanager.data.AniloxFlex
import com.romandruck.colormanager.data.CustomRecipe
import com.romandruck.colormanager.data.RecipeRepository
import com.romandruck.colormanager.data.RecipeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    /* ============================================================
       FLEXO
       ============================================================ */

    private val _flexoRecipes =
        MutableStateFlow<List<CustomRecipe>>(emptyList())

    val flexoRecipes: StateFlow<List<CustomRecipe>> =
        _flexoRecipes.asStateFlow()


    /* ============================================================
       OFFSET
       ============================================================ */

    private val _offsetRecipes =
        MutableStateFlow<List<CustomRecipe>>(emptyList())

    val offsetRecipes: StateFlow<List<CustomRecipe>> =
        _offsetRecipes.asStateFlow()


    /* ============================================================
       ANILOX FLEX
       ============================================================ */

    private val _aniloxRecipes =
        MutableStateFlow<List<AniloxFlex>>(emptyList())

    val aniloxRecipes: StateFlow<List<AniloxFlex>> =
        _aniloxRecipes.asStateFlow()


    /* ============================================================
       SEARCH
       ============================================================ */

    private val _searchQuery =
        MutableStateFlow("")

    val searchQuery: StateFlow<String> =
        _searchQuery.asStateFlow()


    /* ============================================================
       LOADING
       ============================================================ */

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()


    /* ============================================================
       ERROR
       ============================================================ */

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()


    /* ============================================================
       INIT
       ============================================================ */

    init {
        loadAllRecipes()
        loadAllAnilox()
    }


    /* ============================================================
       LOAD ALL RECIPES
       ============================================================ */

    fun loadAllRecipes() {

        /* --------------------------------------------------------
           FLEXO
           -------------------------------------------------------- */

        viewModelScope.launch {

            repository
                .getAllFlexoRecipes()
                .catch { exception ->

                    _error.value =
                        exception.message
                            ?: "Ошибка загрузки FLEXO"

                }
                .collect { recipes ->

                    _flexoRecipes.value = recipes
                }
        }


        /* --------------------------------------------------------
           OFFSET
           -------------------------------------------------------- */

        viewModelScope.launch {

            repository
                .getAllOffsetRecipes()
                .catch { exception ->

                    _error.value =
                        exception.message
                            ?: "Ошибка загрузки OFFSET"

                }
                .collect { recipes ->

                    _offsetRecipes.value = recipes
                }
        }
    }


    /* ============================================================
       LOAD ALL ANILOX
       ============================================================ */

    fun loadAllAnilox() {

        viewModelScope.launch {

            repository
                .getAllAnilox()
                .catch { exception ->

                    _error.value =
                        exception.message
                            ?: "Ошибка загрузки анилоксов"

                }
                .collect { aniloxList ->

                    _aniloxRecipes.value = aniloxList
                }
        }
    }


    /* ============================================================
       SEARCH FLEXO
       ============================================================ */

    fun searchFlexo(
        query: String
    ) {

        _searchQuery.value = query

        viewModelScope.launch {

            repository
                .searchFlexoRecipes(query)
                .catch { exception ->

                    _error.value =
                        exception.message
                            ?: "Ошибка поиска FLEXO"

                }
                .collect { recipes ->

                    _flexoRecipes.value = recipes
                }
        }
    }


    /* ============================================================
       SEARCH OFFSET
       ============================================================ */

    fun searchOffset(
        query: String
    ) {

        _searchQuery.value = query

        viewModelScope.launch {

            repository
                .searchOffsetRecipes(query)
                .catch { exception ->

                    _error.value =
                        exception.message
                            ?: "Ошибка поиска OFFSET"

                }
                .collect { recipes ->

                    _offsetRecipes.value = recipes
                }
        }
    }


    /* ============================================================
       SEARCH ANILOX
       ============================================================ */

    fun searchAnilox(
        query: String
    ) {

        viewModelScope.launch {

            repository
                .searchAnilox(query)
                .catch { exception ->

                    _error.value =
                        exception.message
                            ?: "Ошибка поиска анилокса"

                }
                .collect { aniloxList ->

                    _aniloxRecipes.value = aniloxList
                }
        }
    }


    /* ============================================================
       CLEAR SEARCH
       ============================================================ */

    fun clearSearch() {

        _searchQuery.value = ""

        loadAllRecipes()
        loadAllAnilox()
    }


    /* ============================================================
       ADD RECIPE
       ============================================================ */

    fun addRecipe(
        recipe: CustomRecipe,
        onSuccess: (() -> Unit)? = null
    ) {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null

                when (recipe.type) {

                    RecipeType.FLEXO -> {

                        repository.addFlexoRecipe(
                            recipe
                        )
                    }

                    RecipeType.OFFSET -> {

                        repository.addOffsetRecipe(
                            recipe
                        )
                    }
                }

                onSuccess?.invoke()

            } catch (exception: Exception) {

                _error.value =
                    exception.message
                        ?: "Ошибка сохранения рецепта"

            } finally {

                _isLoading.value = false
            }
        }
    }


    /* ============================================================
       UPDATE RECIPE
       ============================================================ */

    fun updateRecipe(
        recipe: CustomRecipe,
        onSuccess: (() -> Unit)? = null
    ) {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null

                when (recipe.type) {

                    RecipeType.FLEXO -> {

                        repository.updateFlexoRecipe(
                            recipe
                        )
                    }

                    RecipeType.OFFSET -> {

                        repository.updateOffsetRecipe(
                            recipe
                        )
                    }
                }

                onSuccess?.invoke()

            } catch (exception: Exception) {

                _error.value =
                    exception.message
                        ?: "Ошибка изменения рецепта"

            } finally {

                _isLoading.value = false
            }
        }
    }


    /* ============================================================
       DELETE RECIPE
       ============================================================ */

    fun deleteRecipe(
        recipe: CustomRecipe,
        onSuccess: (() -> Unit)? = null
    ) {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null

                when (recipe.type) {

                    RecipeType.FLEXO -> {

                        repository.deleteFlexoRecipe(
                            recipe.name
                        )
                    }

                    RecipeType.OFFSET -> {

                        repository.deleteOffsetRecipe(
                            recipe.name
                        )
                    }
                }

                onSuccess?.invoke()

            } catch (exception: Exception) {

                _error.value =
                    exception.message
                        ?: "Ошибка удаления рецепта"

            } finally {

                _isLoading.value = false
            }
        }
    }


    /* ============================================================
       GET RECIPE
       ============================================================ */

    suspend fun getRecipe(
        recipe: CustomRecipe
    ): CustomRecipe? {

        return when (recipe.type) {

            RecipeType.FLEXO -> {

                repository.getFlexoRecipe(
                    recipe.name
                )
            }

            RecipeType.OFFSET -> {

                repository.getOffsetRecipe(
                    recipe.name
                )
            }
        }
    }


    /* ============================================================
       ADD ANILOX
       ============================================================ */

    fun addAnilox(
        anilox: AniloxFlex,
        onSuccess: (() -> Unit)? = null
    ) {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null

                repository.addAnilox(
                    anilox
                )

                onSuccess?.invoke()

            } catch (exception: Exception) {

                _error.value =
                    exception.message
                        ?: "Ошибка добавления анилокса"

            } finally {

                _isLoading.value = false
            }
        }
    }


    /* ============================================================
       UPDATE ANILOX
       ============================================================ */

    fun updateAnilox(
        anilox: AniloxFlex,
        onSuccess: (() -> Unit)? = null
    ) {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null

                repository.updateAnilox(
                    anilox
                )

                onSuccess?.invoke()

            } catch (exception: Exception) {

                _error.value =
                    exception.message
                        ?: "Ошибка изменения анилокса"

            } finally {

                _isLoading.value = false
            }
        }
    }


    /* ============================================================
       DELETE ANILOX
       ============================================================ */

    fun deleteAnilox(
        anilox: AniloxFlex,
        onSuccess: (() -> Unit)? = null
    ) {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null

                repository.deleteAnilox(
                    anilox
                )

                onSuccess?.invoke()

            } catch (exception: Exception) {

                _error.value =
                    exception.message
                        ?: "Ошибка удаления анилокса"

            } finally {

                _isLoading.value = false
            }
        }
    }


    /* ============================================================
       CLEAR ERROR
       ============================================================ */

    fun clearError() {

        _error.value = null
    }
}


/* ================================================================
   FACTORY
   ================================================================ */

class RecipeViewModelFactory(
    private val repository: RecipeRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                RecipeViewModel::class.java
            )
        ) {

            return RecipeViewModel(
                repository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
