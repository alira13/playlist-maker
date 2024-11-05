package com.example.playlistmaker.screenPlayer.domain

import com.example.playlistmaker.core.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {
    fun setListener(listener: PlayerListener)
    fun prepare(trackSource: String)
    fun play()
    fun isPlaying(): Boolean
    fun pause()
    fun getCurrentTime(): String
    fun quit()
    suspend fun addToFavorites(track: Track)
    suspend fun deleteFromFavorites(track: Track)
    fun favoritesTracks(): Flow<List<Track>>
}