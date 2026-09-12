package com.romandruck.colormanager.ui.pantone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.romandruck.colormanager.data.PantoneItem
import com.romandruck.colormanager.data.PantoneRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PantoneViewModel(
    private val repository: PantoneRepository
) : ViewModel() {

    private val _colors = MutableStateFlow<List<PantoneItem>>(emptyList())
    val colors: StateFlow<List<PantoneItem>> = _colors.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadColors()
    }

    private fun loadColors() {
        viewModelScope.launch {
            repository.getAllColors()
                .catch { exception ->
                    _error.value = exception.message
                    _isLoading.value = false
                }
                .collect { result ->
                    _colors.value = result
                    _isLoading.value = false
                }
        }
    }

    fun search(query: String) {
        _searchQuery.value = query

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.searchColors(query)
                .catch { exception ->
                    _error.value = exception.message
                    _isLoading.value = false
                }
                .collect { result ->
                    _colors.value = result
                    _isLoading.value = false
                }
        }
    }
}

class PantoneViewModelFactory(
    private val repository: PantoneRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(PantoneViewModel::class.java)) {
            return PantoneViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}
