package com.example.sepedain.dataclasses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Time
import java.util.Date

@Parcelize
data class PlaceMap(
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val price: Int = 0,
    val imageId: Int = -1,
    val duration: Time?,
    val date: Date?
) : Parcelable
