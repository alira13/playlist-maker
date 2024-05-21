package com.example.playlistmaker.presentation.playerScreen

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.mapper.TrackMapper
import com.example.playlistmaker.presentation.models.PlaylistInfo
import com.example.playlistmaker.presentation.searchScreen.SearchFragment.Companion.TRACK_VALUE
import com.example.playlistmaker.presentation.ui.PlaylistAdapter
import com.example.playlistmaker.presentation.ui.PlaylistItemClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment(), PlaylistItemClickListener {

    private val playerViewModel by viewModel<PlayerViewModel> { parametersOf(getTrack()) }

    private lateinit var binding: FragmentPlayerBinding
    private var isPlayClickAllowed = true
    private var isLikeClickAllowed = true

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val adapter = PlaylistAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTrack()

        //prepare
        playerViewModel.preparePlayer()
        setTrackInfo()

        binding.likeButton.setOnClickListener {
            val click = likeClickDebounce()
            if (click) {
                playerViewModel.onLikeButtonClicked()
            }
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.playlerPlaylistsRv.adapter = adapter
        binding.addToPlaylistBtn.setOnClickListener {
            showPlaylists()
        }

        binding.newPlaylistBtn.setOnClickListener {
        }

        //play, pause, stop
        binding.playControlButton.setOnClickListener {
            val click = playClickDebounce()

            if (click)
                playerViewModel.onPlayButtonClicked()
        }

        //quit player
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        playerViewModel.isLiked.observe(viewLifecycleOwner) {
            binding.likeButton.isEnabled = isPlayClickAllowed
            binding.likeButton.setImageResource(it.buttonImage)
        }

        playerViewModel.playerState.observe(viewLifecycleOwner) {
            binding.playControlButton.isEnabled = it.isButtonEnabled
            binding.playControlButton.setImageResource(it.buttonImage)
            binding.currentTrackTime.text = it.progress
        }
    }

    private fun getTrack(): Track {
        val track = if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(TRACK_VALUE)!!
        } else {
            arguments?.getParcelable<Track>(TRACK_VALUE)!!
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

        binding.likeButton.setImageResource(playerViewModel.isLiked.value!!.buttonImage)

        Glide.with(binding.playerTrackImage).load(playerTrackInfo.artworkUrl512).fitCenter()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(binding.playerTrackImage.resources.getDimensionPixelSize(R.dimen.player_track_image_corner_radius)))
            .into(binding.playerTrackImage)
    }

    private fun showPlaylists() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.shadowV.visibility = View.VISIBLE

        //TODO Показать плейлисты
        /*
        var playlistsState: PlaylistsState
        binding.playlerPlaylistsRv.isVisible = !playlistsState.isError
        when (playlistsState) {
            is PlaylistsState.ShowPlaylists -> adapter.items =
                playlistsState.playlists.toMutableList()

            else -> {}
        }
        */
    }

    private fun playClickDebounce(): Boolean {
        val current = isPlayClickAllowed
        if (isPlayClickAllowed) {
            isPlayClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isPlayClickAllowed = true
            }
        }
        return current
    }

    private fun likeClickDebounce(): Boolean {
        val current = isLikeClickAllowed
        if (isLikeClickAllowed) {
            isLikeClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isLikeClickAllowed = true
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

    override fun onClick(track: PlaylistInfo) {
        TODO("Not yet implemented")
    }
}