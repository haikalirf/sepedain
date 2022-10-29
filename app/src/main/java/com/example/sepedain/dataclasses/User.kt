package com.example.sepedain.dataclasses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var username: String,
    var email: String,
    var uid: String,
    var firstName: String = "User",
    var lastName: String = "",
    var phoneNumber: String = "",
    var gender: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
): Parcelable