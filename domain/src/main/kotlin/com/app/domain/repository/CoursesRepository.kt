package com.app.domain.repository

import com.app.core.model.Course
import kotlinx.coroutines.flow.Flow

interface CoursesRepository {
    fun observeCourses(): Flow<List<Course>>
    fun observeFavoriteCourses(): Flow<List<Course>>
    suspend fun refreshCourses()
    suspend fun toggleFavorite(courseId: Int)
}
