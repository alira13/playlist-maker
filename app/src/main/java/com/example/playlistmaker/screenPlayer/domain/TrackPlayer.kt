package com.example.playlistmaker.screenPlayer.domain

import com.example.playlistmaker.screenPlayer.domain.PlayerListener

interface TrackPlayer {
    fun setListener(listener: PlayerListener)
    fun prepare(trackSource: String)
    fun play()
    fun isPlaying(): Boolean
    fun pause()
    fun getCurrentTime(): String
    fun quit()
}