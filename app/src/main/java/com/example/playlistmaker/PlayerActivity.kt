package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.SearchActivity.Companion.TRACK_VALUE
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private val mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    private lateinit var playControlButton: ImageButton

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        val track = if (SDK_INT >= 33) {
            intent.getParcelableExtra(TRACK_VALUE, Track::class.java)!!
        } else {
            intent.getParcelableExtra<Track>(TRACK_VALUE)!!
        }

        val trackImage:ImageView = findViewById(R.id.player_track_image)
        val playerTrackName: TextView = findViewById(R.id.player_track_name)
        val playerArtistName: TextView = findViewById(R.id.player_track_author)
        val playerTrackTime: TextView = findViewById(R.id.track_time_value)
        val collectionName: TextView = findViewById(R.id.track_album_value)
        val releaseDate: TextView = findViewById(R.id.track_year_value)
        val primaryGenreName: TextView = findViewById(R.id.track_style_value)
        val country: TextView = findViewById(R.id.track_country_value)
        playControlButton = findViewById(R.id.play_control_button)

        playerTrackName.text = track.trackName
        playerArtistName.text = track.artistName
        playerTrackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
        collectionName.text = track.collectionName
        releaseDate.text = if (track.releaseDate.isNotEmpty()) track.releaseDate.substring(
            0,
            4
        ) else "Not found"
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country

        val artworkUrl512 = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
        Glide
            .with(trackImage)
            .load(artworkUrl512)
            .fitCenter()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(trackImage.resources.getDimensionPixelSize(R.dimen.player_track_image_corner_radius)))
            .into(trackImage)

        preparePlayer(track)
        playControlButton.setOnClickListener { playerButtonControl() }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun playerButtonControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PAUSED, STATE_PREPARED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playControlButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playControlButton.setImageResource(R.drawable.play_button);
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        playControlButton.setImageResource(R.drawable.pause_button);
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        playControlButton.setImageResource(R.drawable.play_button);
    }
}