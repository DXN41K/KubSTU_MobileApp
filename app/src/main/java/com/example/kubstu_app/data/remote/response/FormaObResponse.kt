package com.example.kubstu_app.data.remote.response

import com.example.kubstu_app.data.model.FormaObModel

class FormaObResponse(
    val isok: Boolean,
    val data: List<FormaObModel>?,
    val error: String?
)