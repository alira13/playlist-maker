package com.example.playlistmaker.domain.player

interface TrackPlayer {
    fun setListener(listener: PlayerListener)
    fun prepare(trackSource: String)
    fun play()
    fun isPlaying(): Boolean
    fun pause()
    fun getCurrentTime(): String
    fun quit()
}