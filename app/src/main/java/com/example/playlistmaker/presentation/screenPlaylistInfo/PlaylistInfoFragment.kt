package com.example.playlistmaker.presentation.screenPlaylistInfo

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
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
import com.example.playlistmaker.core.presentation.MainActivity
import com.example.playlistmaker.presentation.screenSearch.SearchFragment
import com.example.playlistmaker.presentation.ui.PlaylistInStringAdapter
import com.example.playlistmaker.presentation.ui.PlaylistItemClickListener
import com.example.playlistmaker.presentation.ui.PlaylistTrackInStringAdapter
import com.example.playlistmaker.presentation.ui.TrackClickListener
import com.example.playlistmaker.presentation.ui.TrackLongClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs

class PlaylistInfoFragment : Fragment(), TrackClickListener, TrackLongClickListener,
    PlaylistItemClickListener {

    private val viewModel by viewModel<PlaylistInfoViewModel> { parametersOf(getPlaylistFromView()) }

    private lateinit var binding: FragmentPlaylistInfoBinding

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val trackAdapter = PlaylistTrackInStringAdapter(this, this)

    private val playlistAdapter = PlaylistInStringAdapter(this)

    private var playlist: Playlist? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.hideBottomNavigation()

        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            binding.shadowV.isVisible = false
        }

        menuBottomSheetBehavior = BottomSheetBehavior.from(binding.moreBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            binding.shadowV.isVisible = false
        }

        binding.playlerPlaylistsRv.adapter = trackAdapter

        binding.currentPlaylistRv.adapter = playlistAdapter

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playlistShareBtn.setOnClickListener {
            sharePlaylist()
        }

        binding.menuBtn.setOnClickListener {
            openMenu()
        }

        viewModel.getPlaylistInfo()

        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }

        binding.sharePlaylistTv.setOnClickListener {
            sharePlaylist()
        }

        binding.deletePlaylistTv.setOnClickListener {
            deletePlaylist()
        }

        binding.editPlaylistTv.setOnClickListener {
            editPlaylist()
        }

        addMenuBottomSheetCallback()
    }

    private fun addMenuBottomSheetCallback() {
        menuBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.shadowV.visibility = View.GONE
                    }

                    else -> {
                        binding.shadowV.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.shadowV.alpha = 1 - abs(slideOffset)
            }
        })
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

        if (playlistInfo.playlist.playlistDescription.isEmpty())
            binding.playlist.isVisible = false
        else {
            binding.playlist.isVisible = true
            binding.playlist.text = playlistInfo.playlist.playlistDescription
        }

        binding.totalDuration.text = resources.getQuantityString(
            R.plurals.total_minutes,
            playlistInfo.totalDuration,
            playlistInfo.totalDuration
        )
        binding.tracksNum.text = resources.getQuantityString(
            R.plurals.track_amount,
            playlistInfo.tracks!!.count(),
            playlistInfo.tracks.count()
        )

        Glide.with(binding.playlistInfoImageIv).load(playlistInfo.playlist.artworkUrl512)
            .fitCenter()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(binding.playlistInfoImageIv.resources.getDimensionPixelSize(R.dimen.player_track_image_corner_radius)))
            .into(binding.playlistInfoImageIv)

        binding.playlerPlaylistsRv.isVisible = true
        trackAdapter.items = playlistInfo.tracks.toMutableList()
        binding.playerMessageTv.isVisible = false
    }

    private fun showEmptyPlaylistInfo(playlistInfo: PlaylistInfo) {
        binding.playlistName.text = playlistInfo.playlist.playlistName

        if (playlistInfo.playlist.playlistDescription.isEmpty())
            binding.playlist.isVisible = false
        else {
            binding.playlist.isVisible = true
            binding.playlist.text = playlistInfo.playlist.playlistDescription
        }

        binding.totalDuration.text = resources.getQuantityString(
            R.plurals.total_minutes,
            playlistInfo.totalDuration,
            playlistInfo.totalDuration
        )
        binding.tracksNum.text = resources.getQuantityString(
            R.plurals.track_amount,
            0,
            0
        )

        Glide.with(binding.playlistInfoImageIv).load(playlistInfo.playlist.artworkUrl512)
            .fitCenter()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(binding.playlistInfoImageIv.resources.getDimensionPixelSize(R.dimen.player_track_image_corner_radius)))
            .into(binding.playlistInfoImageIv)
        binding.playerMessageTv.isVisible = true
        trackAdapter.items = emptyList<Track>().toMutableList()
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
        showSnackbar(requireView(), getString(R.string.msg_app_not_found))
    }

    private fun showNothingToShare() {
        //hideBottomSheet()
        //binding.standardBottomSheet.isVisible = false
        showSnackbar(requireView(), getString(R.string.msg_nothing_to_share))
    }

    private fun sharePlaylist() {
        viewModel.sharePlaylist(requireContext())
        hideMenuBottomSheet()
    }

    private fun deletePlaylist(){
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        ).apply {
            setTitle(R.string.lbl_delete_playlist)
            setMessage(getString(R.string.msg_want_to_delete_playlist))
            setPositiveButton(getString(R.string.btn_yes)) { _, _ ->
                viewModel.deletePlaylist()
            }
            setNegativeButton(R.string.btn_no) { _, _ ->
            }
        }.show()

    }
    private fun editPlaylist() {
        val bundle = Bundle()
        bundle.putParcelable(PLAYLIST_INFO, playlist)
        findNavController().navigate(
            R.id.action_playlistInfoFragment_to_editPlaylistFragment, bundle
        )
        viewModel.getPlaylistInfo()
        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun openMenu() {
        hideBottomSheet()
        showMenuBottomSheet()
    }

    private fun getPlaylistFromView(): Playlist {
        playlist = if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(PLAYLIST_INFO)
        } else {
            arguments?.getParcelable(PLAYLIST_INFO)
        }
        return playlist!!
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.hideBottomNavigation()
        getPlaylistFromView()
        viewModel.getPlaylistInfo()
    }

    override fun onClick(track: Track) {
        val bundle = Bundle()
        bundle.putParcelable(SearchFragment.TRACK_VALUE, track)
        findNavController().navigate(R.id.action_playlistInfoFragment_to_playerFragment, bundle)
    }

    override fun onLongClick(track: Track): Boolean {
        MaterialAlertDialogBuilder(requireContext(),
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        ).apply {
            setTitle(getString(R.string.lbl_delete_track))
            setMessage(getString(R.string.msg_want_to_delete_track))
            setPositiveButton(getString(R.string.btn_delete)) { _, _ ->
                viewModel.deleteTrack(track)
            }
            setNegativeButton(R.string.btn_cancel) { _, _ ->
            }
        }.show()
        return true
    }

    private fun hideMenuBottomSheet() {
        menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.shadowV.isVisible = false
    }

    private fun showMenuBottomSheet() {
        menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.shadowV.isVisible = true
    }

    private fun hideBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    companion object {
        const val PLAYLIST_INFO = "PLAYLIST_INFO"
    }

    override fun onClick(item: Playlist) {
    }
}
