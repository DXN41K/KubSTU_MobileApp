package com.example.kubstu_app.Model

data class KindOfNagr(
    val kindofnagr_id: Int,
    val kindofnagr_name: String
)

data class Disc(
    val disc_id: Int,
    val disc_name: String
)

data class LessonModel(
    val date: String,
    val pair: Int,
    val kindofnagr: KindOfNagr,
    val disc: Disc,
    val classroom: String,
    val teacher: String
)
