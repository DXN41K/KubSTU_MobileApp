package com.example.kubstu_app.data.remote.response

import com.example.kubstu_app.data.model.InstituteModel

class InstituteResponse(
    val isok: Boolean,
    val data: List<InstituteModel>?,
    val error: String?
)