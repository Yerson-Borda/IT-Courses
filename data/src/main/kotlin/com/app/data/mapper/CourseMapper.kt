package com.app.data.mapper

import com.app.core.model.Course
import com.app.core.network.dto.CourseDto

fun CourseDto.toDomain(): Course {
    return Course(
        id = id,
        title = title,
        description = text,
        price = price,
        rating = rate,
        startDate = startDate,
        isLiked = hasLike,
        publishDate = publishDate
    )
}
