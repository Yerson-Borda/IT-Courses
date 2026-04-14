package com.app.data.repository

import com.app.core.database.dao.FavoriteCourseDao
import com.app.core.database.entity.FavoriteCourseEntity
import com.app.core.model.Course
import com.app.core.network.CoursesApi
import com.app.data.mapper.toDomain
import com.app.domain.repository.CoursesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CoursesRepositoryImpl @Inject constructor(
    private val coursesApi: CoursesApi,
    private val favoriteCourseDao: FavoriteCourseDao
) : CoursesRepository {
    private val remoteCourses = MutableStateFlow<List<Course>>(emptyList())

    private val favoriteOverrides = favoriteCourseDao.observeAll().map { entities ->
        entities.associate { entity -> entity.courseId to entity.isFavorite }
    }

    override fun observeCourses(): Flow<List<Course>> {
        return combine(remoteCourses, favoriteOverrides) { courses, overrides ->
            courses.map { course ->
                course.copy(isLiked = overrides[course.id] ?: course.isLiked)
            }
        }
    }

    override fun observeFavoriteCourses(): Flow<List<Course>> {
        return observeCourses().map { courses -> courses.filter { course -> course.isLiked } }
    }

    override suspend fun refreshCourses() {
        if (remoteCourses.value.isNotEmpty()) return
        val response = coursesApi.getCourses()
        remoteCourses.value = response.courses.map { dto -> dto.toDomain() }
    }

    override suspend fun toggleFavorite(courseId: Int) {
        val course = observeCourses().first().firstOrNull { item -> item.id == courseId } ?: return
        favoriteCourseDao.upsert(
            FavoriteCourseEntity(
                courseId = courseId,
                isFavorite = !course.isLiked
            )
        )
    }
}
