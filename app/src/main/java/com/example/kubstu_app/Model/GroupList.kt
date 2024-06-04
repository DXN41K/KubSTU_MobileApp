package com.example.kubstu_app.Model

data class GroupList(
    val isok: Boolean,
    val data: List<GroupModel>,
    val error: String
)