package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.player.PlayerListener

interface PlayerInteractor {
    fun setListener(listener: PlayerListener)
    fun prepare(trackSource: String)
    fun play()
    fun pause()
    fun getCurrentTime(): String
    fun quit()
}