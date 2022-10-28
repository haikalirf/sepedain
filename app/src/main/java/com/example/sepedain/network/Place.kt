package com.example.sepedain.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.squareup.moshi.Json

@Parcelize
data class Place(
    @Json(name="name")
    val name: String,
    @Json(name="lon")
    val lon : Double,
    @Json(name="lat")
    val lat : Double,
//    val street : String = "",
//    val county : String = "",
//    val state : String = "",
//    val postCode : String = "",
//    val country : String = ""
    @Json(name="formatted")
    val formatted : String
): Parcelable

data class PlaceResponse(@Json(name = "features")
val result: List<Place>)