package com.example.playlistmaker.presentation.playlistInfo

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.rootScreen.RootActivity
import com.example.playlistmaker.presentation.searchScreen.SearchFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistInfoFragment : Fragment(), TrackClickListener, TrackLongClickListener {

    private val playerViewModel by viewModel<PlaylistInfoViewModel> { parametersOf(getPlaylist()) }

    private lateinit var binding: FragmentPlaylistInfoBinding

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val adapter = PlaylistTrackInStringAdapter(this, this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? RootActivity)?.hideBottomNavigation()

        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            binding.shadowV.isVisible = false
        }
        binding.playlerPlaylistsRv.adapter = adapter

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        showPlaylistInfo()


    }

    private fun showPlaylistInfo() {
        val playlist = getPlaylist()
        binding.playlistName.text = playlist.playlistName
        binding.playlistDescription.text = playlist.playlistDescription.orEmpty()
        //binding.totalDuration.text
        binding.tracksNum.text = playlist.tracksNum.toString()

        Glide.with(binding.playlistInfoImageIv).load(playlist.artworkUrl512).fitCenter()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(binding.playlistInfoImageIv.resources.getDimensionPixelSize(R.dimen.player_track_image_corner_radius)))
            .into(binding.playlistInfoImageIv)

        showTracks()
    }

    private fun showTracks() {
        playerViewModel.getTracksByIds()

        playerViewModel.tracksState.observe(viewLifecycleOwner) {
            binding.playlerPlaylistsRv.isVisible = it.isNotEmpty()
            if(it.isNotEmpty()) adapter.items = it.toMutableList()
        }
    }

    private fun hideBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun showBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
    private fun getPlaylist(): Playlist {
        val item = if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(PLAYLIST_INFO)
        } else {
            arguments?.getParcelable<Playlist>(PLAYLIST_INFO)
        }
        Log.d("MY", ">> $item")
        return item!!
    }

    override fun onClick(track: Track) {
        //playerViewModel.onPlaylistItemClicked(item)
        val bundle = Bundle()
        bundle.putParcelable(SearchFragment.TRACK_VALUE, track)
        findNavController().navigate(R.id.action_playlistInfoFragment_to_playerFragment, bundle)
    }

    override fun onResume() {
        super.onResume()
        (activity as? RootActivity)?.hideBottomNavigation()
    }

    companion object {
        const val PLAYLIST_INFO = "PLAYLIST_INFO"
    }

    override fun onLongClick(track: Track): Boolean {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.delete_track))
            setMessage(getString(R.string.want_to_delete_track))
            setPositiveButton(getString(R.string.delete)) { _, _ ->
                //playerViewModel.deleteTrack(track)
                //bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
            setNegativeButton(R.string.cancel) { _, _ ->
                //bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }.show()
        return true
    }
}
