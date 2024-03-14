package com.example.playlistmaker.domain.usecases.player

import com.example.playlistmaker.domain.player.PlayerListener
import com.example.playlistmaker.domain.player.TrackPlayer

class PlayerInteractorImpl(private val trackPlayer: TrackPlayer) : PlayerInteractor {

    override fun setListener(listener: PlayerListener) {
        trackPlayer.setListener(listener)
    }

    override fun prepare(trackSource: String) {
        trackPlayer.prepare(trackSource)
    }

    override fun play() {
        trackPlayer.play()
    }

    override fun pause() {
        trackPlayer.pause()
    }

    override fun getCurrentTime(): String {
        return trackPlayer.getCurrentTime()
    }

    override fun quit() {
        trackPlayer.quit()
    }
}