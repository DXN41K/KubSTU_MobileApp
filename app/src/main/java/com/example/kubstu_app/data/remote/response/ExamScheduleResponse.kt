package com.example.kubstu_app.data.remote.response

import com.example.kubstu_app.data.model.ExamScheduleModel

data class ExamScheduleResponse(
    val isok: Boolean,
    val data: List<ExamScheduleModel>?,
    val error_message: String?
)