package com.example.playlistmaker.screenSearch.domain.repository

import com.example.playlistmaker.core.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun getHistory(): Flow<ArrayList<Track>>
    fun addToHistory(track: Track)
    fun clearHistory()
}