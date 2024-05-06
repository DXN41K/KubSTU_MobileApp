package com.example.kubstu_app.Model

import java.util.Date

data class HorizontalDateModel(val date: Date,
                               val day: String,
                               var isSelected: Boolean = false)
