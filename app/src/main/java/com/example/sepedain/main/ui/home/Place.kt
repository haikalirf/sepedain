package com.example.sepedain.main.ui.home

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Place(
    val imageId: Int,
    val placeName: String,
    val distance: Int
)
