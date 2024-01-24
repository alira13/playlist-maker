package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.TrackPlayer
import java.text.SimpleDateFormat
import java.util.Locale

class TrackPlayerImpl : TrackPlayer {

    private val mediaPlayer = MediaPlayer()

    override fun prepare(trackSource: String) {
        mediaPlayer.setDataSource(trackSource)
        mediaPlayer.prepareAsync()
    }

    override fun play() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun getCurrentTime(): String {
        return SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        ).format(mediaPlayer.currentPosition)
    }

    override fun quit() {
        mediaPlayer.release()
    }

    override fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        mediaPlayer.setOnCompletionListener(listener)
    }

    override fun setOnPreparedListener(listener: MediaPlayer.OnPreparedListener) {
        mediaPlayer.setOnPreparedListener(listener)
    }
}