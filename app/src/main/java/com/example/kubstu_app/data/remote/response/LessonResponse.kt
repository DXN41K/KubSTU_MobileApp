package com.example.kubstu_app.data.remote.response

import com.example.kubstu_app.data.model.LessonModel

data class LessonResponse(
    val isok: Boolean,
    val data: List<LessonModel>?,
    val error: String?
)