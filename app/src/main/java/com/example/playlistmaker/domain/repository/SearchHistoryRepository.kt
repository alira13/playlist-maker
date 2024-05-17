package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun getHistory(): Flow<ArrayList<Track>>
    fun addToHistory(track: Track)
    fun clearHistory()
}