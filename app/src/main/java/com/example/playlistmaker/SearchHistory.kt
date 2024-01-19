package com.example.playlistmaker

import com.example.playlistmaker.domain.models.Track

class SearchHistory(private val appSharedPreferences: AppSharedPreferences) {

    private val tracks: ArrayList<Track> = appSharedPreferences.getSearchHistory()

    fun getTracks(): ArrayList<Track> {
        return tracks
    }

    fun addTrack(track: Track) {
        if (tracks.isNotEmpty()) {
            tracks.removeIf { item -> item.trackId == track.trackId }
        }
        if (tracks.size >= TRACK_HISTORY_SIZE) {
            tracks.removeLast()
        }

        tracks.add(0, track)
        appSharedPreferences.putSearchHistory(tracks)
    }

    fun clear() {
        tracks.clear()
        appSharedPreferences.putSearchHistory(tracks)
    }

    private companion object {
        const val TRACK_HISTORY_SIZE = 10
    }
}