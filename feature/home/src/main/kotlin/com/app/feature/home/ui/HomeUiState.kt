package com.app.feature.home.ui

import com.app.core.model.Course

data class HomeUiState(
    val courses: List<Course> = emptyList(),
    val isLoading: Boolean = true,
    val isSortedByDate: Boolean = false
)
