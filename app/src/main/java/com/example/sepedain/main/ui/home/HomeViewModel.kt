package com.example.sepedain.main.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sepedain.main.Repository
import com.example.sepedain.network.ApiClient
import com.example.sepedain.network.Place

class HomeViewModel(
    private val repository: Repository
    = Repository(ApiClient.apiService)
) : ViewModel() {
    private var _placesLiveData = MutableLiveData<List<Place>>()
    val placeLiveData: LiveData<List<Place>>
        get() = _placesLiveData

    private fun fetchPlace() {

    }
}