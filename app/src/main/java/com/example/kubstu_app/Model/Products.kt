package com.example.kubstu_app.Model

data class Products(
    val isok: Boolean,
    val data: List<LessonModel>,
    val error: String
)