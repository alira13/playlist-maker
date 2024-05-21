package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.NetworkResponse
import com.example.playlistmaker.data.dto.TrackNetworkResponse
import com.example.playlistmaker.data.repository.TrackNetworkClient

class TrackRetrofitNetworkClient : TrackNetworkClient {
    override suspend fun search(track: String): NetworkResponse {
        val trackResponse: NetworkResponse
        try {
            val response = RetrofitClient.api.search(track)
            return if (response != null) {
                if (response.results.isEmpty()) {
                    trackResponse = NetworkResponse()
                    trackResponse.resultCount = 0
                } else {
                    trackResponse = TrackNetworkResponse(response.results.toList())
                    trackResponse.resultCount = response.resultCount
                }

                trackResponse
            } else NetworkResponse().apply { resultCount = 400 }
        } catch (ex: Throwable) {
            return NetworkResponse().apply { resultCount = 400 }
        }
    }
}