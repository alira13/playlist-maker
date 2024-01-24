package com.example.playlistmaker.domain.player

import android.media.MediaPlayer

interface TrackPlayer {
    fun prepare(trackSource: String)
    fun play()
    fun pause()
    fun getCurrentTime(): String
    fun quit()
    fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener)
    fun setOnPreparedListener(listener: MediaPlayer.OnPreparedListener)
}