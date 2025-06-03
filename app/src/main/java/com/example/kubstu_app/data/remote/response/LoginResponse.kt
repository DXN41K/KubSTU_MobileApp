package com.example.kubstu_app.data.remote.response

data class LoginResponse(
    val id: Int,
    val fam: String,
    val imja: String,
    val otch: String,
    val gr: String,
    val kurs: Int,
    val inst_id: Int,
    val formaob_id: Int,
    val access_token: String,
    val refresh_token: String,
    val spec_id: Int
)