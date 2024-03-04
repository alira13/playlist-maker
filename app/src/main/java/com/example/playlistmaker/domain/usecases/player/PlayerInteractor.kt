package com.example.playlistmaker.domain.usecases.player

import com.example.playlistmaker.domain.player.PlayerListener

interface PlayerInteractor {
    fun setListener(listener: PlayerListener)
    fun prepare(trackSource: String)
    fun play()
    fun pause()
    fun getCurrentTime(): String
    fun quit()
}