package com.example.playlistmaker.presentation.playerScreen

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.mapper.TrackMapper
import com.example.playlistmaker.presentation.searchScreen.SearchFragment.Companion.TRACK_VALUE
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {

    private val playerViewModel by viewModel<PlayerViewModel> { parametersOf(getTrack(intent)) }

    private lateinit var binding: ActivityPlayerBinding
    private var isClickAllowed = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getTrack(intent)
        //prepare
        playerViewModel.preparePlayer()
        setTrackInfo()

        //play, pause, stop
        binding.playControlButton.setOnClickListener {
            val cl = clickDebounce()
            Log.d("MY_LOG", "clickAllowed = $cl")
            if (cl)
                playerViewModel.onPlayButtonClicked()
        }

        //quit player
        binding.backButton.setOnClickListener {
            finish()
        }

        playerViewModel.playerState.observe(this) {
            binding.playControlButton.isEnabled = it.isPlayButtonEnabled
            binding.playControlButton.setImageResource(it.buttonImage)
            binding.currentTrackTime.text = it.progress
        }
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

    override fun onPause() {
        super.onPause()
        playerViewModel.pause()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}
