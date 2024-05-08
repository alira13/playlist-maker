package com.example.playlistmaker.presentation.playerScreen

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerListener
import com.example.playlistmaker.presentation.mapper.TrackMapper
import com.example.playlistmaker.presentation.searchScreen.SearchFragment.Companion.TRACK_VALUE
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {
    private var playerState = PlayerState.STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    private val playerViewModel by viewModel<PlayerViewModel> { parametersOf(getTrack(intent)) }

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var track = getTrack(intent)
        //prepare
        preparePlayer()
        setTrackInfo()

        //play, pause, stop
        binding.playControlButton.setOnClickListener {
            if (clickDebounce()) playerButtonControl()
        }

        //quit player
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.quit()
        stopTimer()
    }


    private fun getTrack(intent: Intent): Track {
        val track = if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK_VALUE, Track::class.java)!!
        } else {
            intent.getParcelableExtra<Track>(TRACK_VALUE)!!
        }
        return track
    }

    private fun setTrackInfo() {
        val playerTrackInfo = TrackMapper.map(playerViewModel.screenStateLiveData.value!!)

        binding.playerTrackName.text = playerTrackInfo.trackName
        binding.playerTrackAuthor.text = playerTrackInfo.artistName
        binding.trackTimeValue.text = playerTrackInfo.trackTime
        binding.trackAlbumValue.text = playerTrackInfo.collectionName
        binding.trackYearValue.text = playerTrackInfo.releaseDate
        binding.trackStyleValue.text = playerTrackInfo.primaryGenreName
        binding.trackCountryValue.text = playerTrackInfo.country

        Glide.with(binding.playerTrackImage).load(playerTrackInfo.artworkUrl512).fitCenter()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(binding.playerTrackImage.resources.getDimensionPixelSize(R.dimen.player_track_image_corner_radius)))
            .into(binding.playerTrackImage)
    }

    private fun playerButtonControl() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                Log.d("MY_LOG", "pausePlayer")
                pausePlayer()
            }

            PlayerState.STATE_PAUSED, PlayerState.STATE_PREPARED -> {
                Log.d("MY_LOG", "startPlayer")
                startPlayer()
            }

            else -> Unit
        }
    }

    private fun preparePlayer() {
        val playerListener = object : PlayerListener {
            override fun onPrepare() {
                binding.playControlButton.isEnabled = true
                playerState = PlayerState.STATE_PREPARED
            }

            override fun onCompletion() {
                binding.playControlButton.setImageResource(R.drawable.play_button)
                playerState = PlayerState.STATE_PREPARED
                binding.currentTrackTime.text = "00.00"
                stopTimer()
            }
        }
        playerViewModel.preparePlayer(playerListener)
    }

    private fun startPlayer() {
        playerViewModel.play()
        playerState = PlayerState.STATE_PLAYING
        binding.playControlButton.setImageResource(R.drawable.pause_button)
        startTimer()
    }

    private fun pausePlayer() {
        playerViewModel.pause()
        playerState = PlayerState.STATE_PAUSED
        binding.playControlButton.setImageResource(R.drawable.play_button)
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
                        binding.currentTrackTime.text = playerViewModel.getCurrentTime()
                    }

                    PlayerState.STATE_PREPARED -> {
                        binding.currentTrackTime.text = "00:00"
                    }

                    else -> {}
                }
                handler.postDelayed(this, UPDATE_TIMER_DELAY_MILLIS)
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
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val UPDATE_TIMER_DELAY_MILLIS = 1000L
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}
