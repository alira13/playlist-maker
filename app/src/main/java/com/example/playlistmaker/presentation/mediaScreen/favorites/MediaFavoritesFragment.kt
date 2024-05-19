package com.example.playlistmaker.presentation.mediaScreen.favorites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.databinding.FragmentMediaFavoritesBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.playerScreen.PlayerActivity
import com.example.playlistmaker.presentation.ui.ItemClickListener
import com.example.playlistmaker.presentation.ui.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFavoritesFragment : Fragment(), ItemClickListener {

    private var binding: FragmentMediaFavoritesBinding? = null

    private val trackAdapter = TrackAdapter(this)

    private val playerViewModel by viewModel<MediaFavoritesViewModel>()

    private var isClickAllowed = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaFavoritesBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.trackListRv?.adapter = trackAdapter

        playerViewModel.getState()

        playerViewModel.screenStateLiveData.observe(viewLifecycleOwner) {
            binding?.emptyMediaErrorTv?.isVisible = it.isEmpty
            binding?.trackListRv?.isVisible = !it.isEmpty
            trackAdapter.items = it.tracks.toMutableList()
        }
    }

    override fun onResume() {
        super.onResume()
        playerViewModel.getState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }

    override fun onClick(track: Track) {
        //if (clickDebounce()) {
            Intent(requireContext(), PlayerActivity::class.java).apply {
                putExtra(TRACK_VALUE, track)
                startActivity(this)
            }
        //}
    }

    private fun clickDebounce(): Boolean {
Log.d("MY_LOG", "click")
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

    companion object {
        const val TRACK_VALUE = "TRACK_VALUE"
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }
}