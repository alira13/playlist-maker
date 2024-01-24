package com.example.playlistmaker.domain.usecases

import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import com.example.playlistmaker.domain.player.TrackPlayer

class PlayerInteractorImpl(private val trackPlayer: TrackPlayer) : PlayerInteractor {

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

    override fun setOnCompletionListener(listener: OnCompletionListener) {
        trackPlayer.setOnCompletionListener(listener)
    }

    override fun setOnPreparedListener(listener: OnPreparedListener) {
        trackPlayer.setOnPreparedListener(listener)
    }
}