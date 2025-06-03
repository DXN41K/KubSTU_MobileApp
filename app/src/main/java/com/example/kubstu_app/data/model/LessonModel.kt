package com.example.kubstu_app.data.model

data class KindOfNagrModel(
    val kindofnagr_id: Int,
    val kindofnagr_name: String
)

data class DiscModel(
    val disc_id: Int,
    val disc_name: String
)

data class LessonModel(
    val date: String,
    val pair: Int,
    val kindofnagr: KindOfNagrModel,
    val disc: DiscModel,
    val classroom: String,
    val teacher: String
)
