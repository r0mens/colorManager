package com.romandruck.colormanager.ui.anilox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romandruck.colormanager.data.AniloxFlex
import com.romandruck.colormanager.data.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AniloxFlexViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _aniloxList =
        MutableStateFlow<List<AniloxFlex>>(emptyList())

    val aniloxList: StateFlow<List<AniloxFlex>> =
        _aniloxList.asStateFlow()


    private val _searchQuery =
        MutableStateFlow("")

    val searchQuery: StateFlow<String> =
        _searchQuery.asStateFlow()


    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()


    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()


    init {
        loadAll()
    }


    fun loadAll() {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                repository
                    .getAllAnilox()
                    .collect { list ->

                        _aniloxList.value = list
                        _isLoading.value = false
                    }

            } catch (e: Exception) {

                _isLoading.value = false
                _error.value =
                    e.message ?: "Ошибка загрузки"
            }
        }
    }


    fun search(query: String) {

        _searchQuery.value = query

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                repository
                    .searchAnilox(query)
                    .collect { list ->

                        _aniloxList.value = list
                        _isLoading.value = false
                    }

            } catch (e: Exception) {

                _isLoading.value = false
                _error.value =
                    e.message ?: "Ошибка поиска"
            }
        }
    }


    fun clearSearch() {

        _searchQuery.value = ""

        loadAll()
    }


    fun addAnilox(
        name: String,
        volume: Double?
    ) {

        viewModelScope.launch {

            try {

                repository.addAnilox(

                    AniloxFlex(
                        name = name,
                        volume = volume
                    )
                )

            } catch (e: Exception) {

                _error.value =
                    e.message ?: "Ошибка добавления"
            }
        }
    }


    fun updateAnilox(
        name: String,
        volume: Double?
    ) {

        viewModelScope.launch {

            try {

                repository.updateAnilox(

                    AniloxFlex(
                        name = name,
                        volume = volume
                    )
                )

            } catch (e: Exception) {

                _error.value =
                    e.message ?: "Ошибка изменения"
            }
        }
    }


    fun deleteAnilox(
        anilox: AniloxFlex
    ) {

        viewModelScope.launch {

            try {

                repository.deleteAnilox(anilox)

            } catch (e: Exception) {

                _error.value =
                    e.message ?: "Ошибка удаления"
            }
        }
    }
}
