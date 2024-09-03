package com.example.kubstu_app.Model.timeTable

import com.example.kubstu_app.Model.LessonModel

data class Products(
    val isok: Boolean,
    val data: List<LessonModel>,
    val error: String
)