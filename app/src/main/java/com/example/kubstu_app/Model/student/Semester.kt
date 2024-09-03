package com.example.kubstu_app.Model.student

data class Semester(
    val title: String,
    val e: List<Exam>,
    val z: List<Zachet>
)

data class Exam(
    val tipkontr_id: Int,
    val tipkontr_lname: String,
    val disc_name: String,
    val oc_f: Int,
    val id: Int,
    val work_name: String?,
    val filename: String?,
    val file_id: Int?,
    val student_id: Int
)

data class Zachet(
    val tipkontr_id: Int,
    val tipkontr_lname: String,
    val disc_name: String,
    val oc_f: Int,
    val id: Int,
    val work_name: String?,
    val filename: String?,
    val file_id: Int?,
    val student_id: Int
)