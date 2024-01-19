package com.example.playlistmaker

import com.example.playlistmaker.domain.models.Track
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApiService {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponse>
}

class TrackResponse(
    val resultCount: Int,
    val results: List<Track>
)