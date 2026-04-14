package com.app.core.network

import com.app.core.network.dto.CoursesResponseDto
import retrofit2.http.GET

interface CoursesApi {
    @GET("courses")
    suspend fun getCourses(): CoursesResponseDto
}
