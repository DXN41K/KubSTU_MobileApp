package com.example.kubstu_app.presentation.viewstate

data class SvcCardViewState(
    val Title: String,
    val SubTitle: String,
    val Background: Int,
    var Image: Int? = null,
    val Url: String? = null,
)