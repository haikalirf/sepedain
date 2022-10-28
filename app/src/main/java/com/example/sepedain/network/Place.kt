package com.example.sepedain.network

import kotlinx.android.parcel.Parcelize
import com.squareup.moshi.Json

@Parcelize
data class Place(
    @Json(name="name")
    val name: String = "",
    @Json(name="lon")
    val lon : Double = 0.0,
    @Json(name="lat")
    val lat : Double = 0.0,
//    val street : String = "",
//    val county : String = "",
//    val state : String = "",
//    val postCode : String = "",
//    val country : String = ""
    @Json(name="formatted")
    val formatted : String = ""
)

data class PlaceResponse(@Json(name = "features")
val result: List<Place>)