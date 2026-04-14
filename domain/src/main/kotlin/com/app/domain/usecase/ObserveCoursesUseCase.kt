package com.app.domain.usecase

import com.app.core.model.Course
import com.app.domain.repository.CoursesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCoursesUseCase @Inject constructor(
    private val repository: CoursesRepository
) {
    operator fun invoke(): Flow<List<Course>> = repository.observeCourses()
}
