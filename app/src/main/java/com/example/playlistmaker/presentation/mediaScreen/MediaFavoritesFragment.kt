package com.example.playlistmaker.presentation.mediaScreen

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
        Log.d("VIEW", "onCreateView {$binding}")
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.trackListRv?.adapter = trackAdapter
        Log.d("VIEW", "onViewCreated {$binding}")
        playerViewModel.getState()

        playerViewModel.screenStateLiveData.observe(viewLifecycleOwner) {
            binding?.emptyMediaErrorTv?.isVisible = it.isEmpty
            binding?.trackListRv?.isVisible = !it.isEmpty
            trackAdapter.items = it.tracks.toMutableList()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("VIEW", "onResume {$binding}")
        playerViewModel.getState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        Log.d("VIEW", "onDestroyView {$binding}")
    }

    override fun onClick(track: Track) {
        Log.d("MY_LOG", "view: onClick")
        if (clickDebounce()) {
            Intent(requireContext(), PlayerActivity::class.java).apply {
                putExtra(TRACK_VALUE, track)
                startActivity(this)
            }
        }
    }

    private fun clickDebounce(): Boolean {
        Log.d("MY_LOG", "view: clickDebounce")
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                Log.d("MY_LOG", "view 1: clickDebounce")
                delay(CLICK_DEBOUNCE_DELAY)
                Log.d("MY_LOG", "view 2: clickDebounce")
                isClickAllowed = true
                Log.d("MY_LOG", "view 3: clickDebounce")
            }
        }
        return current
    }

    companion object {
        const val TRACK_VALUE = "TRACK_VALUE"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}