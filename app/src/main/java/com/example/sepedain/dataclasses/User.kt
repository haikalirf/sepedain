package com.example.sepedain.dataclasses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var username: String,
    var email: String,
    var uid: String,
    var firstName: String,
    var lastName: String?,
    var phoneNumber: String?,
    var gender: String?,
    var recentlyVisited: Array<PlaceMap>?,
    var lastKnownLocation: String?
): Parcelable