package com.app.feature.favorites.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.domain.usecase.ObserveFavoriteCoursesUseCase
import com.app.domain.usecase.RefreshCoursesUseCase
import com.app.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesViewModel(
    observeFavoriteCoursesUseCase: ObserveFavoriteCoursesUseCase,
    private val refreshCoursesUseCase: RefreshCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {
    val uiState: StateFlow<FavoritesUiState> = observeFavoriteCoursesUseCase().map { courses ->
        FavoritesUiState(
            courses = courses,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FavoritesUiState()
    )

    init {
        viewModelScope.launch {
            refreshCoursesUseCase()
        }
    }

    fun onToggleFavorite(courseId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(courseId)
        }
    }

    class Factory @Inject constructor(
        private val observeFavoriteCoursesUseCase: ObserveFavoriteCoursesUseCase,
        private val refreshCoursesUseCase: RefreshCoursesUseCase,
        private val toggleFavoriteUseCase: ToggleFavoriteUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
                return FavoritesViewModel(
                    observeFavoriteCoursesUseCase = observeFavoriteCoursesUseCase,
                    refreshCoursesUseCase = refreshCoursesUseCase,
                    toggleFavoriteUseCase = toggleFavoriteUseCase
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
