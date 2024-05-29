package com.example.playlistmaker.presentation.playlistInfo

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
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
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistInfoFragment : Fragment(), TrackClickListener, TrackLongClickListener {

    private val viewModel by viewModel<PlaylistInfoViewModel> { parametersOf(getPlaylistFromView()) }

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

        binding.playlistShareBtn.setOnClickListener {
            sharePlaylist()
        }

        Log.d("MY", "renderState")
        viewModel.getPlaylistInfo()
        Log.d("MY", ">>renderState")
        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(state: PlaylistInfoState) {
        when (state) {
            is PlaylistInfoState.NotEmpty -> showPlaylistInfo(state.playlistInfo)
            is PlaylistInfoState.Empty -> showEmptyPlaylistInfo(state.playlistInfo)
            is PlaylistInfoState.PlaylistDeleted -> findNavController().navigateUp()
            is PlaylistInfoState.NoApplicationFound -> if (!state.feedbackWasShown) showNoApplicationFound()
            is PlaylistInfoState.NothingToShare -> if (!state.feedbackWasShown) showNothingToShare()
        }
    }

    private fun showPlaylistInfo(playlistInfo: PlaylistInfo) {
        binding.playlistName.text = playlistInfo.playlist.playlistName
        binding.playlistDescription.text = playlistInfo.playlist.playlistDescription
        binding.totalDuration.text = resources.getQuantityString(
            R.plurals.total_minutes,
            playlistInfo.totalDuration,
            playlistInfo.totalDuration
        )
        binding.tracksNum.text = resources.getQuantityString(
            R.plurals.track_amount,
            playlistInfo.tracks!!.count(),
            playlistInfo.tracks!!.count()
        )

        Glide.with(binding.playlistInfoImageIv).load(playlistInfo.playlist.artworkUrl512)
            .fitCenter()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(binding.playlistInfoImageIv.resources.getDimensionPixelSize(R.dimen.player_track_image_corner_radius)))
            .into(binding.playlistInfoImageIv)

        binding.playlerPlaylistsRv.isVisible = true
        adapter.items = playlistInfo.tracks.toMutableList()
    }

    private fun showEmptyPlaylistInfo(playlistInfo: PlaylistInfo) {
    }

    private fun showSnackbar(root: View, text: String) {
        val snackbar = Snackbar.make(root, text, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(
            ContextCompat.getColor(
                root.context, R.color.YP_grey
            )
        )
        snackbar.setTextColor(
            ContextCompat.getColor(
                root.context, R.color.YP_white
            )
        )
        val textView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackbar.show()
    }

    private fun showNoApplicationFound() {

    }

    private fun showNothingToShare() {
        showSnackbar(requireView(), getString(R.string.nothing_to_share))
    }

    private fun sharePlaylist() {
        viewModel.sharePlaylist(requireContext())
    }

    private fun editPlaylist() {
    }


    private fun getPlaylistFromView(): Playlist {
        val item = if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(PLAYLIST_INFO)
        } else {
            arguments?.getParcelable<Playlist>(PLAYLIST_INFO)
        }
        Log.d("MY", ">> $item")
        return item!!
    }

    override fun onResume() {
        super.onResume()
        (activity as? RootActivity)?.hideBottomNavigation()
    }

    override fun onClick(track: Track) {
        val bundle = Bundle()
        bundle.putParcelable(SearchFragment.TRACK_VALUE, track)
        findNavController().navigate(R.id.action_playlistInfoFragment_to_playerFragment, bundle)
    }

    override fun onLongClick(track: Track): Boolean {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.delete_track))
            setMessage(getString(R.string.want_to_delete_track))
            setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteTrack(track)
            }
            setNegativeButton(R.string.cancel) { _, _ ->
            }
        }.show()
        return true
    }

    companion object {
        const val PLAYLIST_INFO = "PLAYLIST_INFO"
    }
}
