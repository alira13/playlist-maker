package com.example.playlistmaker.screenSearch.domain.usecases

import com.example.playlistmaker.core.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryInteractor {
    fun getHistory(): Flow<ArrayList<Track>>
    fun addToHistory(track: Track)
    fun clearHistory()
}