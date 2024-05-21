package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.sharedPreferences.AppSharedPreferences
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchHistoryRepositoryImpl(
    private val appSharedPreferences: AppSharedPreferences,
    private val appDatabase: AppDatabase,
) : SearchHistoryRepository {

    private lateinit var tracks: ArrayList<Track>

    override fun getHistory(): Flow<ArrayList<Track>> = flow {
        tracks = appSharedPreferences.getSearchHistory()
        val ids = appDatabase.getTrackDao().getTrackIds()
        tracks.map { it.isFavorite = ids.contains(it.trackId) }
        emit(tracks)
    }

    override fun addToHistory(track: Track) {
        if (tracks.isNotEmpty()) {
            tracks.removeIf { item -> item.trackId == track.trackId }
        }
        if (tracks.size >= TRACK_HISTORY_SIZE) {
            tracks.removeLast()
        }

        tracks.add(0, track)
        appSharedPreferences.putSearchHistory(tracks)
    }

    override fun clearHistory() {
        tracks.clear()
        appSharedPreferences.putSearchHistory(tracks)
    }

    private companion object {
        const val TRACK_HISTORY_SIZE = 10
    }
}