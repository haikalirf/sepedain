package com.example.sepedain.main.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PlaceDao {
    private val placeList = mutableListOf<Place>()
    private val places = MutableLiveData<List<Place>>()

    init {
        places.value = placeList
    }

    fun addPlace(place: Place) {
        placeList.add(place)
        places.value = placeList
    }

    fun getPlaces() = places as LiveData<List<Place>>
}