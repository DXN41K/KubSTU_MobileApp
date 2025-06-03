package com.example.kubstu_app.data.model

data class SemesterModel(
    val title: String,
    val e: List<ExamModel>,
    val z: List<ZachetModel>
)

data class ExamModel(
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

data class ZachetModel(
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