package com.example.playlistmaker.domain.usecases.search

import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun execute(text: String): Flow<Pair<List<Track>?, ConsumerData<Track>?>>
}