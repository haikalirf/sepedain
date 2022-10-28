package com.example.sepedain.main.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sepedain.dataclasses.PlaceMap

class HomeViewModel : ViewModel() {

    lateinit var recyclerListData: MutableLiveData<PlaceMap>

    init {
        recyclerListData = MutableLiveData()
    }

    fun getRecyclerListDataObserver(): MutableLiveData<PlaceMap> {
        return recyclerListData
    }

}