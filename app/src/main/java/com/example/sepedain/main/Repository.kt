package com.example.sepedain.main

import com.example.sepedain.network.ApiService

class Repository(private val apiService: ApiService) {
    fun getPlaces(
        categories: String,
        filter: String,
        bias: String,
        limit: String,
        apiKey: String
    ) = apiService.fetchPlaces(
        categories,
        filter,
        bias,
        limit,
        apiKey
    )
}