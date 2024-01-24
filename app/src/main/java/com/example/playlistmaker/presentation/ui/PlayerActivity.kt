package com.example.playlistmaker.presentation.ui

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.SearchActivity.Companion.TRACK_VALUE
import com.example.playlistmaker.domain.usecases.PlayerInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.mapper.TrackMapper

class PlayerActivity : AppCompatActivity() {

    private val playerIteractor: PlayerInteractor = Creator.provideTrackInteractor()

    private var playerState = PlayerState.STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    private lateinit var playControlButton: ImageButton
    private lateinit var currentTrackTime: TextView
    private lateinit var trackImage: ImageView
    private lateinit var playerTrackName: TextView
    private lateinit var playerArtistName: TextView
    private lateinit var playerTrackTime: TextView
    private lateinit var collectionName: TextView
    private lateinit var releaseDate: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var country: TextView
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        getViews()

        val track = getTrack(intent)
        setTrackInfo(track)

        preparePlayer(track)

        playControlButton.setOnClickListener {
            if (clickDebounce()) playerButtonControl()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerIteractor.quit()
        stopTimer()
    }

    private fun getViews() {
        backButton = findViewById(R.id.back_button)
        trackImage = findViewById(R.id.player_track_image)
        playerTrackName = findViewById(R.id.player_track_name)
        playerArtistName = findViewById(R.id.player_track_author)
        playerTrackTime = findViewById(R.id.track_time_value)
        collectionName = findViewById(R.id.track_album_value)
        releaseDate = findViewById(R.id.track_year_value)
        primaryGenreName = findViewById(R.id.track_style_value)
        country = findViewById(R.id.track_country_value)
        playControlButton = findViewById(R.id.play_control_button)
        currentTrackTime = findViewById(R.id.current_track_time)
    }

    private fun getTrack(intent: Intent): Track {
        val track = if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK_VALUE, Track::class.java)!!
        } else {
            intent.getParcelableExtra<Track>(TRACK_VALUE)!!
        }
        return track
    }

    private fun setTrackInfo(track: Track) {
        val playerTrackInfo = TrackMapper.map(track)
        playerTrackName.text = playerTrackInfo.trackName
        playerArtistName.text = playerTrackInfo.artistName
        playerTrackTime.text = playerTrackInfo.trackTime
        collectionName.text = playerTrackInfo.collectionName
        releaseDate.text = playerTrackInfo.releaseDate
        primaryGenreName.text = playerTrackInfo.primaryGenreName
        country.text = playerTrackInfo.country

        Glide.with(trackImage).load(playerTrackInfo.artworkUrl512).fitCenter().placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(trackImage.resources.getDimensionPixelSize(R.dimen.player_track_image_corner_radius)))
            .into(trackImage)
    }

    private fun playerButtonControl() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> pausePlayer()
            PlayerState.STATE_PAUSED, PlayerState.STATE_PREPARED -> startPlayer()
            else -> Unit
        }
    }

    private fun preparePlayer(track: Track) {
        playerIteractor.prepare(track.previewUrl)
        playerIteractor.setOnPreparedListener {
            playControlButton.isEnabled = true
            playerState = PlayerState.STATE_PREPARED
        }
        playerIteractor.setOnCompletionListener {
            playControlButton.setImageResource(R.drawable.play_button)
            playerState = PlayerState.STATE_PREPARED
            currentTrackTime.text = "00.00"
            stopTimer()
        }
    }

    private fun startPlayer() {
        playerIteractor.play()
        playerState = PlayerState.STATE_PLAYING
        playControlButton.setImageResource(R.drawable.pause_button)
        startTimer()
    }

    private fun pausePlayer() {
        playerIteractor.pause()
        playerState = PlayerState.STATE_PAUSED
        playControlButton.setImageResource(R.drawable.play_button)
        stopTimer()
    }

    private fun startTimer() {
        handler.post(createUpdateTimerTask())
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                when (playerState) {
                    PlayerState.STATE_PLAYING -> {
                        currentTrackTime.text = playerIteractor.getCurrentTime()
                    }

                    PlayerState.STATE_PREPARED -> {
                        currentTrackTime.text = "00:00"
                    }

                    else -> {}
                }
                handler.postDelayed(this, UPDATE_TIMER_DELAY)
            }
        }
    }

    private fun stopTimer() {
        handler.removeCallbacks(createUpdateTimerTask())
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    enum class PlayerState {
        STATE_DEFAULT, STATE_PREPARED, STATE_PLAYING, STATE_PAUSED
    }

    companion object {
        private const val UPDATE_TIMER_DELAY = 1000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
