package com.example.sepedain.main.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sepedain.main.Repository
import com.example.sepedain.main.ScreenState
import com.example.sepedain.main.ui.home.HomeFragment
import com.example.sepedain.network.ApiClient
import com.example.sepedain.network.Place
import com.example.sepedain.network.PlaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel(
    private val repository: Repository
    = Repository(ApiClient.apiService)
) : ViewModel() {
    private var _placesLiveData = MutableLiveData<ScreenState<List<Place>?>>()
    val placeLiveData: LiveData<ScreenState<List<Place>?>>
        get() = _placesLiveData

    init {
        fetchPlace()
    }

    private fun fetchPlace () {
        val client = repository.getPlaces(
            "commercial",
            "geometry:0ad2ca57a82b4a5ab2919dc0a5e93711",
            "proximity:${HomeFragment().longitude},${HomeFragment().latitude}",
//            "proximity:112.6240242,-7.960171",
            "20",
            "ec51bcde20554127ac97cc4c52eff067"
        )
        client.enqueue(object : Callback<PlaceResponse> {
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                if (response.isSuccessful) {
                    Log.d("place", "" + response.body())
                    _placesLiveData.postValue(ScreenState.Success(response.body()?.result))
                } else {
                    _placesLiveData.postValue(ScreenState.Error(response.code().toString(), null))
                }
            }

            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                Log.d("Failure", t.message.toString())
            }
        })
    }
}