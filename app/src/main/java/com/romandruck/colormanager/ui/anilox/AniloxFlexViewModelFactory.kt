package com.romandruck.colormanager.ui.anilox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romandruck.colormanager.data.RecipeRepository

class AniloxFlexViewModelFactory(
    private val repository: RecipeRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                AniloxFlexViewModel::class.java
            )
        ) {

            return AniloxFlexViewModel(
                repository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}
