package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun getHistory(): ArrayList<Track>
    fun addToHistory(track: Track)
    fun clearHistory()
}