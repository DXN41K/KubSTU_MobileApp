package com.example.kubstu_app.Model.student

data class Work(
    val id: Int,
    val ugod: Int,
    val semestr: Int,
    val student_id: Int,
    val tipkontr_id: Int,
    val tipkontr_name: String,
    val oc_f: Int,
    val work_name: String,
    val filename: String,
    val file_id: Int
)