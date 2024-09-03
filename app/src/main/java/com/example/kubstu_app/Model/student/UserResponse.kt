package com.example.kubstu_app.Model.student

data class UserResponse(
    val id: Int,
    val fam: String,
    val imja: String,
    val otch: String?,
    val gr: String,
    val kurs: Int,
    val inst_id: Int,
    val formaob_id: Int
)