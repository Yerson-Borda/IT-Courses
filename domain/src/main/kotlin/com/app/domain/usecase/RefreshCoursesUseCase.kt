package com.app.domain.usecase

import com.app.domain.repository.CoursesRepository
import javax.inject.Inject

class RefreshCoursesUseCase @Inject constructor(
    private val repository: CoursesRepository
) {
    suspend operator fun invoke() {
        repository.refreshCourses()
    }
}
