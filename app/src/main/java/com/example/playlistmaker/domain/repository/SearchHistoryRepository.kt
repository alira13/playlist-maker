package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun getHistory(): ArrayList<Track>
    fun addToHistory(track: Track)
    fun clearHistory()
}