package com.romandruck.colormanager.ui.inklibrary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.romandruck.colormanager.data.InkLibrary
import com.romandruck.colormanager.data.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class InkLibraryViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    /* ============================================================
       INKS
       ============================================================ */

    private val _inks =
        MutableStateFlow<List<InkLibrary>>(emptyList())

    val inks: StateFlow<List<InkLibrary>> =
        _inks.asStateFlow()


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
        loadAllInks()
    }


    /* ============================================================
       LOAD ALL
       ============================================================ */

    fun loadAllInks() {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            repository
                .getAllInks()
                .catch { exception ->

                    _error.value =
                        exception.message
                            ?: "Ошибка загрузки библиотеки красок"

                    _isLoading.value = false
                }
                .collect { inkList ->

                    _inks.value = inkList
                    _isLoading.value = false
                }
        }
    }


    /* ============================================================
       SEARCH
       ============================================================ */

    fun search(
        query: String
    ) {

        _searchQuery.value = query

        if (query.isBlank()) {

            loadAllInks()

            return
        }

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            repository
                .searchInks(query)
                .catch { exception ->

                    _error.value =
                        exception.message
                            ?: "Ошибка поиска красок"

                    _isLoading.value = false
                }
                .collect { inkList ->

                    _inks.value = inkList
                    _isLoading.value = false
                }
        }
    }


    /* ============================================================
       ADD
       ============================================================ */

    fun addInk(
        ink: InkLibrary,
        onSuccess: (() -> Unit)? = null
    ) {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null

                repository.addInk(ink)

                onSuccess?.invoke()

            } catch (exception: Exception) {

                _error.value =
                    exception.message
                        ?: "Ошибка добавления краски"

            } finally {

                _isLoading.value = false
            }
        }
    }


    /* ============================================================
       UPDATE
       ============================================================ */

    fun updateInk(
        ink: InkLibrary,
        onSuccess: (() -> Unit)? = null
    ) {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null

                repository.updateInk(ink)

                onSuccess?.invoke()

            } catch (exception: Exception) {

                _error.value =
                    exception.message
                        ?: "Ошибка изменения краски"

            } finally {

                _isLoading.value = false
            }
        }
    }


    /* ============================================================
       DELETE
       ============================================================ */

    fun deleteInk(
        ink: InkLibrary,
        onSuccess: (() -> Unit)? = null
    ) {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null

                repository.deleteInk(ink)

                onSuccess?.invoke()

            } catch (exception: Exception) {

                _error.value =
                    exception.message
                        ?: "Ошибка удаления краски"

            } finally {

                _isLoading.value = false
            }
        }
    }


    /* ============================================================
       GET BY ID
       ============================================================ */

    suspend fun getInkById(
        id: Int
    ): InkLibrary? {

        return repository.getInkById(id)
    }


    /* ============================================================
       CLEAR SEARCH
       ============================================================ */

    fun clearSearch() {

        _searchQuery.value = ""

        loadAllInks()
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

class InkLibraryViewModelFactory(
    private val repository: RecipeRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                InkLibraryViewModel::class.java
            )
        ) {

            return InkLibraryViewModel(
                repository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
