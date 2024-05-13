package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.TrackNetworkResponse
import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val trackNetworkClient: TrackNetworkClient
) : SearchRepository {

    override fun search(text: String): Flow<Resource<List<Track>>> = flow {
        val response = trackNetworkClient.search(text)
        if (response is TrackNetworkResponse) {
            val tracks = response.results
            emit(Resource.Success(tracks))
        } else if (response.resultCount == 400) {
            emit(Resource.NetworkError())
        } else {
            emit(Resource.EmptyListError())
        }
    }
}