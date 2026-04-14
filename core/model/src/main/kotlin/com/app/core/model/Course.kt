package com.app.core.model

data class Course(
    val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val rating: String,
    val startDate: String,
    val isLiked: Boolean,
    val publishDate: String
)
