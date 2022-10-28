package com.example.sepedain.dataclasses

import java.util.Date
import java.util.Timer

data class PlaceMap(
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val price: Int,
    val imageId: Int,
    val duration: Timer,
    val date: Date
)
