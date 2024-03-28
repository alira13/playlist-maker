package com.example.playlistmaker.presentation.searchScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.playerScreen.PlayerActivity
import com.example.playlistmaker.presentation.ui.ItemClickListener
import com.example.playlistmaker.presentation.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), ItemClickListener, SearchView {
    private lateinit var binding: FragmentSearchBinding

    private val searchViewModel by viewModel<SearchViewModel>()

    private var isClickAllowed = true

    private val trackAdapter = TrackAdapter(this)
    private val historyTrackAdapter = TrackAdapter(this)

    private val handler: Handler = Handler(Looper.getMainLooper())

    private var simpleTextWatcher: TextWatcher? = null

    private var trackListRv: RecyclerView? = null
    private var historyTrackListRv: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("MY_LOG", "SearchFragment onCreateView start")
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        Log.d("MY_LOG", "SearchFragment onCreateView finish")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("MY_LOG", "SearchFragment onViewCreated start")
        try {
            super.onViewCreated(view, savedInstanceState)

            binding.trackListRv.adapter = trackAdapter
            historyTrackListRv?.adapter = historyTrackAdapter

            searchViewModel.stateLiveData.observe(viewLifecycleOwner) {
                render(it)
            }

            binding.backBtn.setOnClickListener {
                activity?.finish();
            }

            binding.clearSearchRequestIv.setOnClickListener {
                binding.searchRequestEt.setText("")
                binding.clearSearchRequestIv.isVisible = false

                searchViewModel.getHistory()
                searchViewModel.stateLiveData.observe(viewLifecycleOwner) {
                    render(it)
                }
            }

            binding.searchRequestEt.doOnTextChanged { text, start, before, count ->
                Log.d("MY_LOG", "view: doOnTextChanged")
                searchViewModel.searchDebounce(changedText = text?.toString() ?: "")
            }

            binding.searchRequestEt.doAfterTextChanged { text: Editable? ->
                Log.d("MY_LOG", "view: afterTextChanged")
                val inputMethodManager =
                    activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as? InputMethodManager
                if (text.isNullOrEmpty()) {
                    inputMethodManager?.hideSoftInputFromWindow(
                        binding.searchRequestEt.windowToken, 0
                    )
                    //при пустом поле поиска показываем историю, если она не пустая
                    searchViewModel.getHistory()
                    searchViewModel.stateLiveData.observe(viewLifecycleOwner) {
                        render(it)
                    }
                } else {
                    //скрываем историю треков как только начинаем печатать что-то в поиске
                    showEmpty()
                    binding.clearSearchRequestIv.isVisible = true
                    inputMethodManager?.showSoftInput(binding.searchRequestEt, 0)
                }
            }

            binding.searchRequestEt.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && binding.searchRequestEt.text.isEmpty() && historyTrackAdapter.items.isNotEmpty()) {
                    showTrackHistory(historyTrackAdapter.items)
                } else {
                    showTrackList(trackAdapter.items)
                }
            }

            binding.retrySearchBtn.setOnClickListener {
                searchViewModel.search(binding.searchRequestEt.text.toString())
            }

            binding.clearHistoryBtn.setOnClickListener {
                searchViewModel.clearHistory()
            }

        } catch (ex: Exception) {
            Log.d("MY_LOG", ex.message.toString(), ex.fillInStackTrace())
        }
    }

    override fun render(state: SearchState) {
        Log.d("MY_LOG", "view:render: $state")
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
        Log.d("MY_LOG", "view: showLoading")
        binding.emptyListErrorTv.isVisible = false
        binding.connectionErrorTv.isVisible = false
        binding.retrySearchBtn.isVisible = false

        binding.trackHistoryTv.isVisible = false
        binding.clearHistoryBtn.isVisible = false
        historyTrackListRv?.isVisible = false

        binding.searchPbr.isVisible = true
    }

    private fun showTrackList(tracks: List<Track>) {
        Log.d("MY_LOG", "view: showTrackList")
        binding.trackHistoryTv.isVisible = false
        binding.clearHistoryBtn.isVisible = false
        historyTrackListRv?.isVisible = false


        binding.emptyListErrorTv.isVisible = false
        binding.connectionErrorTv.isVisible = false
        binding.retrySearchBtn.isVisible = false

        binding.searchPbr.isVisible = false
        trackListRv?.isVisible = true
        trackAdapter.items = tracks.toMutableList()
    }

    private fun showTrackHistory(tracks: List<Track>) {
        Log.d("MY_LOG", "view: showTrackHistory")

        binding.emptyListErrorTv.isVisible = false
        binding.connectionErrorTv.isVisible = false
        binding.retrySearchBtn.isVisible = false

        trackListRv?.isVisible = false

        //if (tracks.isNotEmpty()) {
        binding.trackHistoryTv.isVisible = true
        binding.clearHistoryBtn.isVisible = true
        historyTrackListRv?.isVisible = true
        //}
        historyTrackAdapter.items = tracks.toMutableList()
    }

    private fun showEmptyTrackHistory() {
        Log.d("MY_LOG", "view: showEmptyTrackHistory")
        historyTrackAdapter.clearItems()
        binding.trackHistoryTv.isVisible = false
        binding.clearHistoryBtn.isVisible = false
        historyTrackListRv?.isVisible = false
    }

    private fun showConnectionError() {
        Log.d("MY_LOG", "view: showConnectionError")
        binding.searchPbr.isVisible = false
        trackAdapter.clearItems()

        binding.trackHistoryTv.isVisible = false
        binding.clearHistoryBtn.isVisible = false
        historyTrackListRv?.isVisible = false

        trackListRv?.isVisible = false
        binding.connectionErrorTv.isVisible = true
        binding.retrySearchBtn.isVisible = true
    }

    private fun showEmptyTrackListError() {
        Log.d("MY_LOG", "view: showEmptyTrackListError")
        trackAdapter.clearItems()

        binding.trackHistoryTv.isVisible = false
        binding.clearHistoryBtn.isVisible = false
        historyTrackListRv?.isVisible = false

        binding.searchPbr.isVisible = false
        trackListRv?.isVisible = false
        binding.emptyListErrorTv.isVisible = true
    }

    private fun showEmpty() {
        Log.d("MY_LOG", "view: showEmpty")
        binding.trackHistoryTv.isVisible = false
        binding.clearHistoryBtn.isVisible = false
        historyTrackListRv?.isVisible = false
    }

    companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
        const val TRACK_VALUE = "TRACK_VALUE"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onClick(track: Track) {
        Log.d("MY_LOG", "view: onClick")
        if (clickDebounce()) {
            searchViewModel.addToHistory(track)
            historyTrackAdapter.notifyDataSetChanged()
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
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("MY_LOG", "view: onDestroy")
        simpleTextWatcher?.let { binding.searchRequestEt.removeTextChangedListener(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("MY_LOG", "view: onSaveInstanceState")
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, binding.searchRequestEt.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("MY_LOG", "view: onRestoreInstanceState")
        val searchValue = savedInstanceState?.getString(SEARCH_VALUE, "")
        binding.searchRequestEt.setText(searchValue)
    }
}