package com.example.kubstu_app.Domains

import android.media.Image
import android.widget.ImageView

data class SpecialDomain(val Title: String,
                         val SubTitle: String,
                         val Background: Int,
                         var Image: Int? = null)