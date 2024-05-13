package com.example.playlistmaker.domain.usecases.search

import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(private val trackRepository: SearchRepository) : SearchInteractor {
    override fun execute(text: String): Flow<Pair<List<Track>?, ConsumerData<Track>?>> {
        return trackRepository.search(text).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, ConsumerData.Data(result.data))
                }

                is Resource.NetworkError -> {
                    Pair(null, ConsumerData.NetworkError(""))
                }

                is Resource.EmptyListError -> {
                    Pair(null, ConsumerData.EmptyListError(""))
                }
            }
        }
    }
}