package com.example.playlistmaker.domain.usecases.player

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerListener
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