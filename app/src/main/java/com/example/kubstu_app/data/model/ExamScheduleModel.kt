package com.example.kubstu_app.data.model

data class ExamScheduleModel(
    val date_sd: String,
    val time_sd: String,
    val disc: DiscModel,
    val classroom: String,
    val teacher: String
)