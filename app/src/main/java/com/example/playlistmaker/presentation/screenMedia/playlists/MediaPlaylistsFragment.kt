package com.example.playlistmaker.presentation.screenMedia.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaPlaylistsBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.core.presentation.MainActivity
import com.example.playlistmaker.presentation.ui.PlaylistAdapter
import com.example.playlistmaker.presentation.ui.PlaylistItemClickListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlaylistsFragment : Fragment(), PlaylistItemClickListener {

    private val viewModel by viewModel<MediaPlaylistsViewModel>()

    private var binding: FragmentMediaPlaylistsBinding? = null

    private val adapter = PlaylistAdapter(this)

    private var isClickAllowed = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaPlaylistsBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.showBottomNavigation()

        binding?.createPlaylistButtonBtn?.setOnClickListener {
            if (clickDebounce()) {
                createBtnClickListener()
            }
        }

        binding?.playlistsRv?.adapter = adapter
        binding?.playlistsRv?.layoutManager = GridLayoutManager(activity, 2)

        viewModel.getState()
        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: PlaylistsState) {
        binding?.emptyPlaylistsErrorTv?.isVisible = state.isError
        when (state) {
            is PlaylistsState.ShowPlaylists -> {
                binding?.playlistsRv?.isVisible = true
                adapter.items = state.playlists.toMutableList()
            }

            is PlaylistsState.EmptyPlaylists -> {
                binding?.playlistsRv?.isVisible = false
            }
        }
    }


    private fun createBtnClickListener() {
        findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showBottomNavigation()
        viewModel.getState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        const val PLAYLIST_INFO = "PLAYLIST_INFO"
    }


    override fun onClick(item: Playlist) {
        val bundle = Bundle()
        bundle.putParcelable(PLAYLIST_INFO, item)
        findNavController().navigate(R.id.action_mediaFragment_to_playlistInfoFragment, bundle)
    }
}