package com.example.sepedain.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Place(
    @Json(name = "properties")
    val properties : Properties
) : Parcelable

@Parcelize
data class Properties (
    @Json(name = "name")
    val name : String?,
    @Json(name = "lon")
    val lon : Double,
    @Json(name = "lat")
    val lat : Double,
    @Json(name = "formatted")
    val formatted : String?,
    @Json(name = "distance")
    val distance : Int
) : Parcelable

data class PlaceResponse(
    @Json(name = "features")
    val result: List<Place>
)