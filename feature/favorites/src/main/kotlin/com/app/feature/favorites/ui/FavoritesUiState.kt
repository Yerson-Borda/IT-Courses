package com.app.feature.favorites.ui

import com.app.core.model.Course

data class FavoritesUiState(
    val courses: List<Course> = emptyList(),
    val isLoading: Boolean = true
)
