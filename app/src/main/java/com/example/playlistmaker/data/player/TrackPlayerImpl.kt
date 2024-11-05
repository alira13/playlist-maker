package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.screenPlayer.domain.PlayerListener
import com.example.playlistmaker.screenPlayer.domain.TrackPlayer
import java.text.SimpleDateFormat
import java.util.Locale

class TrackPlayerImpl : TrackPlayer {

    private val mediaPlayer = MediaPlayer()
    private lateinit var listener: PlayerListener
    override fun setListener(listener: PlayerListener) {
        this.listener = listener
    }

    override fun prepare(trackSource: String) {
        mediaPlayer.setDataSource(trackSource)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            listener.onPrepare()
        }
        mediaPlayer.setOnCompletionListener {
            listener.onCompletion()
        }
    }

    override fun play() {
        mediaPlayer.start()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
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
        mediaPlayer.reset()
    }
}