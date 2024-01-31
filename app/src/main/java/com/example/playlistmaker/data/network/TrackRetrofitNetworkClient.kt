package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.NetworkResponse
import com.example.playlistmaker.data.dto.TrackNetworkResponse
import com.example.playlistmaker.data.repository.TrackNetworkClient

class TrackRetrofitNetworkClient: TrackNetworkClient {
    override fun search(currency: String): NetworkResponse {
        val trackResponse:NetworkResponse
        try {
            val response = RetrofitClient.api.search(currency).execute()
            if (response.isSuccessful) {
                if (response.body()!!.results.isEmpty()) {
                    trackResponse = NetworkResponse()
                    trackResponse.resultCount = 0
                } else {
                    trackResponse = TrackNetworkResponse(response.body()!!.results.toList())
                    trackResponse.resultCount = response.body()!!.resultCount
                }
                return trackResponse
            }
            else return NetworkResponse().apply { resultCount = 400 }
        } catch (ex: Exception) {
            return NetworkResponse().apply { resultCount = 400 }
        }
    }
}