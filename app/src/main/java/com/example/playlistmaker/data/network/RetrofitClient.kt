package com.example.playlistmaker.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val TRACK_SEARCH_URL = "https://itunes.apple.com/"

    private val client: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(TRACK_SEARCH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: TrackSearchApi by lazy {
        client.create(TrackSearchApi::class.java)
    }
}