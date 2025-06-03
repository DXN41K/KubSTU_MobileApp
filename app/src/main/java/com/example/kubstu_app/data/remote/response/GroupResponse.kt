package com.example.kubstu_app.data.remote.response

import com.example.kubstu_app.data.model.GroupModel

data class GroupResponse(
    val isok: Boolean,
    val data: List<GroupModel>?,
    val error: String?
)