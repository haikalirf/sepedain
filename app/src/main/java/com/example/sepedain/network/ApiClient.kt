package com.example.sepedain.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object ApiClient {

    private val BASE_URL = "https://api.geoapify.com/v2/places?categories=commercial&filter=geometry:0ad2ca57a82b4a5ab2919dc0a5e93711&bias=proximity:112.6659335,-7.8931151&limit=20&apiKey=ec51bcde20554127ac97cc4c52eff067"

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

interface ApiService {
    @GET("place")
    fun fetchPlace(@Query("page") page: String): Call<PlaceResponse>
}