package com.example.kubstu_app.Model.dic

data class SpecsResponse(
    val isok: Boolean,
    val data: List<SpecsModel>,
    val error_message: String?
)
