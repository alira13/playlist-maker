package com.example.playlistmaker.screenSearch.domain.usecases

import com.example.playlistmaker.screenSearch.domain.consumer.ConsumerData
import com.example.playlistmaker.core.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun execute(text: String): Flow<Pair<List<Track>?, ConsumerData<Track>?>>
}