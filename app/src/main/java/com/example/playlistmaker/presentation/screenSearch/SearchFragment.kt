package com.example.playlistmaker.presentation.screenSearch

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.screenMain.MainActivity
import com.example.playlistmaker.presentation.ui.ItemClickListener
import com.example.playlistmaker.presentation.ui.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), ItemClickListener, SearchView {
    private var binding: FragmentSearchBinding? = null

    private val searchViewModel by viewModel<SearchViewModel>()

    private var isClickAllowed = true

    private val trackAdapter = TrackAdapter(this)
    private val historyTrackAdapter = TrackAdapter(this)

    private var simpleTextWatcher: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.showBottomNavigation()

        binding?.trackListRv?.adapter = trackAdapter
        binding?.historyTrackListRv?.adapter = historyTrackAdapter

        searchViewModel.stateLiveData.observe(viewLifecycleOwner) {
            render(it)
        }

        binding?.clearSearchRequestIv?.setOnClickListener {
            binding?.searchRequestEt?.setText("")
            binding?.clearSearchRequestIv?.isVisible = false

            searchViewModel.getHistory()
            searchViewModel.stateLiveData.observe(viewLifecycleOwner) {
                render(it)
            }
        }

        binding?.searchRequestEt?.doOnTextChanged { text, start, before, count ->
            searchViewModel.searchDebounce(changedText = text?.toString() ?: "")
        }

        binding?.searchRequestEt?.doAfterTextChanged { text: Editable? ->
            val inputMethodManager =
                activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as? InputMethodManager
            if (text.isNullOrEmpty()) {
                inputMethodManager?.hideSoftInputFromWindow(
                    binding?.searchRequestEt?.windowToken, 0
                )
                //при пустом поле поиска показываем историю, если она не пустая
                searchViewModel.getHistory()
                searchViewModel.stateLiveData.observe(viewLifecycleOwner) {
                    render(it)
                }
            } else {
                //скрываем историю треков как только начинаем печатать что-то в поиске
                showEmpty()
                binding?.clearSearchRequestIv?.isVisible = true
                inputMethodManager?.showSoftInput(binding?.searchRequestEt, 0)
            }
        }

        binding?.searchRequestEt?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding!!.searchRequestEt.text.isEmpty() && historyTrackAdapter.items.isNotEmpty()) {
                showTrackHistory(historyTrackAdapter.items)
            } else {
                showTrackList(trackAdapter.items)
            }
        }

        binding?.retrySearchBtn?.setOnClickListener {
            searchViewModel.search(binding?.searchRequestEt?.text.toString())
        }

        binding?.clearHistoryBtn?.setOnClickListener {
            searchViewModel.clearHistory()
        }
    }

    override fun render(state: SearchState) {
        when (state) {
            SearchState.Loading -> showLoading()
            is SearchState.TrackHistory -> showTrackHistory(state.tracks)
            is SearchState.TrackList -> showTrackList(state.tracks)

            SearchState.EmptyTrackHistory -> showEmptyTrackHistory()
            SearchState.ConnectionError -> showConnectionError()
            SearchState.EmptyTrackListError -> showEmptyTrackListError()
        }
    }

    private fun showLoading() {
        binding?.emptyListErrorTv?.isVisible = false
        binding?.connectionErrorTv?.isVisible = false
        binding?.retrySearchBtn?.isVisible = false

        binding?.trackHistoryTv?.isVisible = false
        binding?.clearHistoryBtn?.isVisible = false
        binding?.historyTrackListRv?.isVisible = false

        binding?.searchPbr?.isVisible = true
    }

    private fun showTrackList(tracks: List<Track>) {
        binding?.trackHistoryTv?.isVisible = false
        binding?.clearHistoryBtn?.isVisible = false
        binding?.historyTrackListRv?.isVisible = false

        binding?.emptyListErrorTv?.isVisible = false
        binding?.connectionErrorTv?.isVisible = false
        binding?.retrySearchBtn?.isVisible = false

        binding?.searchPbr?.isVisible = false
        binding?.trackListRv?.isVisible = true
        trackAdapter.items = tracks.toMutableList()
    }

    private fun showTrackHistory(tracks: List<Track>) {
        binding?.emptyListErrorTv?.isVisible = false
        binding?.connectionErrorTv?.isVisible = false
        binding?.retrySearchBtn?.isVisible = false

        binding?.trackListRv?.isVisible = false

        if (tracks.isNotEmpty()) {
            binding?.trackHistoryTv?.isVisible = true
            binding?.clearHistoryBtn?.isVisible = true
            binding?.historyTrackListRv?.isVisible = true
            historyTrackAdapter.items = tracks.toMutableList()
        }
    }

    private fun showEmptyTrackHistory() {
        historyTrackAdapter.clearItems()
        binding?.trackHistoryTv?.isVisible = false
        binding?.clearHistoryBtn?.isVisible = false
        binding?.historyTrackListRv?.isVisible = false
    }

    private fun showConnectionError() {
        binding?.searchPbr?.isVisible = false
        trackAdapter.clearItems()

        binding?.trackHistoryTv?.isVisible = false
        binding?.clearHistoryBtn?.isVisible = false
        binding?.historyTrackListRv?.isVisible = false

        binding?.trackListRv?.isVisible = false
        binding?.connectionErrorTv?.isVisible = true
        binding?.retrySearchBtn?.isVisible = true
    }

    private fun showEmptyTrackListError() {
        trackAdapter.clearItems()

        binding?.trackHistoryTv?.isVisible = false
        binding?.clearHistoryBtn?.isVisible = false
        binding?.historyTrackListRv?.isVisible = false

        binding?.searchPbr?.isVisible = false
        binding?.trackListRv?.isVisible = false
        binding?.emptyListErrorTv?.isVisible = true
    }

    private fun showEmpty() {
        binding?.trackHistoryTv?.isVisible = false
        binding?.clearHistoryBtn?.isVisible = false
        binding?.historyTrackListRv?.isVisible = false
    }

    companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
        const val TRACK_VALUE = "TRACK_VALUE"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onClick(track: Track) {
        if (clickDebounce()) {
            searchViewModel.addToHistory(track)
            historyTrackAdapter.notifyDataSetChanged()

            val bundle = Bundle()
            bundle.putParcelable(TRACK_VALUE, track)
            findNavController().navigate(R.id.action_searchFragment_to_playerFragment, bundle)
        }
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
        searchViewModel.getHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        simpleTextWatcher?.let { binding?.searchRequestEt?.removeTextChangedListener(it) }
        binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, binding?.searchRequestEt?.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val searchValue = savedInstanceState?.getString(SEARCH_VALUE, "")
        binding?.searchRequestEt?.setText(searchValue)
    }
}