package com.example.playlistmaker.domain.usecases.search

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryInteractor {
    fun getHistory(): Flow<ArrayList<Track>>
    fun addToHistory(track: Track)
    fun clearHistory()
}