package com.example.playlistmaker.domain.usecases.player

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerListener

interface PlayerInteractor {
    fun setListener(listener: PlayerListener)
    fun prepare(trackSource: String)
    fun play()
    fun isPlaying(): Boolean
    fun pause()
    fun getCurrentTime(): String
    fun quit()
    fun isLiked(track: Track):Boolean
    fun addToFavorites(track: Track)
    fun deleteFromFavorites(track: Track)
}