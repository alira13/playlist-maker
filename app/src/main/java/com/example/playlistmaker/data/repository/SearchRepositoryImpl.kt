package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.TrackNetworkResponse
import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val trackNetworkClient: TrackNetworkClient
) : SearchRepository {

    override fun search(text: String): Resource<List<Track>> {
        val response = trackNetworkClient.search(text)

        return if (response is TrackNetworkResponse) {
            val tracks = response.results
            Resource.Success(tracks)
        } else if(response.resultCount==400){
            Resource.NetworkError()
        }
        else {
            Resource.EmptyListError()
        }
    }
}