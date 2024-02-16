package com.example.playlistmaker.domain.player

interface TrackPlayer {
    fun setListener(listener:PlayerListener)
    fun prepare(trackSource: String)
    fun play()
    fun pause()
    fun getCurrentTime(): String
    fun quit()
}