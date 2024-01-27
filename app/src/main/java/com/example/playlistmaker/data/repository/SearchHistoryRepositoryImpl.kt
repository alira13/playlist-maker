package com.example.playlistmaker.data.repository

import android.content.Context
import com.example.playlistmaker.data.sharedPreferences.AppSharedPreferences
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryRepositoryImpl(private val context: Context) :
    SearchHistoryRepository {
    private lateinit var tracks: ArrayList<Track>

    override fun getHistory(): ArrayList<Track> {
        tracks = (context as AppSharedPreferences).getSearchHistory()
        return tracks
    }

    override fun addToHistory(track: Track) {
        if (tracks.isNotEmpty()) {
            tracks.removeIf { item -> item.trackId == track.trackId }
        }
        if (tracks.size >= TRACK_HISTORY_SIZE) {
            tracks.removeLast()
        }

        tracks.add(0, track)
        (context as AppSharedPreferences).putSearchHistory(tracks)
    }

    override fun clearHistory() {
        tracks.clear()
        (context as AppSharedPreferences).putSearchHistory(tracks)
    }

    private companion object {
        const val TRACK_HISTORY_SIZE = 10
    }
}