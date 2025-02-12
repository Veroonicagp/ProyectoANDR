package com.example.readytoenjoy.core.model

import android.net.Uri

data class Activity(
    val id: String,
    val title: String,
    val img: Uri?=null,
    val location: String,
    val price: String,
    val description: String,
    val advenId: String?=null
)
