package com.app.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.domain.usecase.ObserveCoursesUseCase
import com.app.domain.usecase.RefreshCoursesUseCase
import com.app.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel(
    observeCoursesUseCase: ObserveCoursesUseCase,
    private val refreshCoursesUseCase: RefreshCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {
    private val sortByPublishDate = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> = combine(
        observeCoursesUseCase(),
        sortByPublishDate
    ) { courses, shouldSort ->
        HomeUiState(
            courses = if (shouldSort) {
                courses.sortedByDescending { course -> course.publishDate }
            } else {
                courses
            },
            isLoading = false,
            isSortedByDate = shouldSort
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    init {
        viewModelScope.launch {
            refreshCoursesUseCase()
        }
    }

    fun onSortByDateClick() {
        sortByPublishDate.value = true
    }

    fun onToggleFavorite(courseId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(courseId)
        }
    }

    class Factory @Inject constructor(
        private val observeCoursesUseCase: ObserveCoursesUseCase,
        private val refreshCoursesUseCase: RefreshCoursesUseCase,
        private val toggleFavoriteUseCase: ToggleFavoriteUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(
                    observeCoursesUseCase = observeCoursesUseCase,
                    refreshCoursesUseCase = refreshCoursesUseCase,
                    toggleFavoriteUseCase = toggleFavoriteUseCase
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
