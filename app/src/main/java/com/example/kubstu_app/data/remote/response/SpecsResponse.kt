package com.example.kubstu_app.data.remote.response

import com.example.kubstu_app.data.model.SpecsModel

data class SpecsResponse(
    val isok: Boolean,
    val data: List<SpecsModel>?,
    val error_message: String?
)
