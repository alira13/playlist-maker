package com.example.playlistmaker.presentation.searchScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.playerScreen.PlayerActivity
import com.example.playlistmaker.presentation.ui.ItemClickListener
import com.example.playlistmaker.presentation.ui.TrackAdapter

class SearchActivity : AppCompatActivity(), ItemClickListener, SearchView {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchViewModel: SearchViewModel

    private var isClickAllowed = true

    private val trackAdapter = TrackAdapter(this)
    private val historyTrackAdapter = TrackAdapter(this)

    private val handler: Handler = Handler(Looper.getMainLooper())

    private var simpleTextWatcher: TextWatcher? = null

    private lateinit var trackListRv: RecyclerView
    private lateinit var historyTrackListRv: RecyclerView

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            Log.d("MY_LOG", "view: onCreate")
            super.onCreate(savedInstanceState)

            binding = ActivitySearchBinding.inflate(layoutInflater)
            setContentView(binding.root)

            searchViewModel = ViewModelProvider(
                this, SearchViewModel.getViewModelFactory()
            )[SearchViewModel::class.java]

            searchViewModel.stateLiveData.observe(this) {
                render(it)
            }

            binding.backBtn.setOnClickListener {
                finish()
            }

            binding.clearSearchRequestIv.setOnClickListener {
                binding.searchRequestEt.setText("")
                binding.clearSearchRequestIv.isVisible = false
                showTrackHistory()
            }

            trackListRv = findViewById(R.id.track_list_rv)
            trackListRv.apply {
                adapter = trackAdapter
            }

            binding.searchRequestEt.doOnTextChanged { text, start, before, count ->
                Log.d("MY_LOG", "view: doOnTextChanged")
                searchViewModel.searchDebounce(changedText = text?.toString() ?: "")
            }

            binding.searchRequestEt.doAfterTextChanged { text: Editable? ->
                Log.d("MY_LOG", "view: afterTextChanged")
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                if (text.isNullOrEmpty()) {
                    inputMethodManager?.hideSoftInputFromWindow(
                        binding.searchRequestEt.windowToken, 0
                    )
                    //при пустом поле поиска показываем историю, если она не пустая
                    showTrackHistory()
                } else {
                    //скрываем историю треков как только начинаем печатать что-то в поиске
                    showEmpty()
                    binding.clearSearchRequestIv.isVisible = true
                    inputMethodManager?.showSoftInput(binding.searchRequestEt, 0)
                }
            }

            binding.searchRequestEt.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && binding.searchRequestEt.text.isEmpty() && historyTrackAdapter.items.isNotEmpty()) {
                    historyTrackAdapter.items = searchViewModel.getHistory()
                    historyTrackAdapter.notifyDataSetChanged()
                    showTrackHistory()
                } else {
                    showTrackList(historyTrackAdapter.items)
                }
            }

            binding.retrySearchBtn.setOnClickListener {
                searchViewModel.search(binding.searchRequestEt.text.toString())
            }

            historyTrackListRv = findViewById(R.id.history_track_list_rv)

            historyTrackListRv.apply {
                adapter = historyTrackAdapter
            }

            historyTrackAdapter.items = searchViewModel.getHistory()

            binding.clearHistoryBtn.setOnClickListener {
                searchViewModel.clearHistory()
            }

        } catch (ex: Exception) {
            Log.d("MY_LOG", ex.message.toString(), ex.fillInStackTrace())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("MY_LOG", "view: onSaveInstanceState")
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, binding.searchRequestEt.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d("MY_LOG", "view: onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
        val searchValue = savedInstanceState.getString(SEARCH_VALUE, "")
        binding.searchRequestEt.setText(searchValue)
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(track: Track) {
        Log.d("MY_LOG", "view: onClick")
        if (clickDebounce()) {
            searchViewModel.addToHistory(track)
            historyTrackAdapter.notifyDataSetChanged()
            Intent(this, PlayerActivity::class.java).apply {
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

    override fun onDestroy() {
        Log.d("MY_LOG", "view: onDestroy")
        super.onDestroy()
        simpleTextWatcher?.let { binding.searchRequestEt.removeTextChangedListener(it) }
    }

    override fun render(state: SearchState) {
        Log.d("MY_LOG", "view: render")
        when (state) {
            SearchState.Loading -> showLoading()
            is SearchState.TrackList -> showTrackList(state.tracks)
            SearchState.TrackHistory -> showTrackHistory()
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
        historyTrackListRv.isVisible = false

        binding.searchPbr.isVisible = true
    }

    private fun showTrackList(tracks: List<Track>) {
        Log.d("MY_LOG", "view: showTrackList")
        binding.trackHistoryTv.isVisible = false
        binding.clearHistoryBtn.isVisible = false
        historyTrackListRv.isVisible = false


        binding.emptyListErrorTv.isVisible = false
        binding.connectionErrorTv.isVisible = false
        binding.retrySearchBtn.isVisible = false

        binding.searchPbr.isVisible = false
        trackListRv.isVisible = true
        trackAdapter.items = tracks.toMutableList()
    }

    private fun showTrackHistory() {
        Log.d("MY_LOG", "view: showTrackHistory")
        binding.emptyListErrorTv.isVisible = false
        binding.connectionErrorTv.isVisible = false
        binding.retrySearchBtn.isVisible = false

        trackListRv.isVisible = false

        if (historyTrackAdapter.items.isNotEmpty()) {
            binding.trackHistoryTv.isVisible = true
            binding.clearHistoryBtn.isVisible = true
            historyTrackListRv.isVisible = true
        }
    }

    private fun showEmptyTrackHistory() {
        Log.d("MY_LOG", "view: showEmptyTrackHistory")
        historyTrackAdapter.clearItems()
        binding.trackHistoryTv.isVisible = false
        binding.clearHistoryBtn.isVisible = false
        historyTrackListRv.isVisible = false
    }

    private fun showConnectionError() {
        Log.d("MY_LOG", "view: showConnectionError")
        binding.searchPbr.isVisible = false
        trackAdapter.clearItems()

        binding.trackHistoryTv.isVisible = false
        binding.clearHistoryBtn.isVisible = false
        historyTrackListRv.isVisible = false

        trackListRv.isVisible = false
        binding.connectionErrorTv.isVisible = true
        binding.retrySearchBtn.isVisible = true
    }

    private fun showEmptyTrackListError() {
        Log.d("MY_LOG", "view: showEmptyTrackListError")
        trackAdapter.clearItems()

        binding.trackHistoryTv.isVisible = false
        binding.clearHistoryBtn.isVisible = false
        historyTrackListRv.isVisible = false

        binding.searchPbr.isVisible = false
        trackListRv.isVisible = false
        binding.emptyListErrorTv.isVisible = true
    }

    private fun showEmpty() {
        Log.d("MY_LOG", "view: showEmpty")
        binding.trackHistoryTv.isVisible = false
        binding.clearHistoryBtn.isVisible = false
        historyTrackListRv.isVisible = false
    }

    companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
        const val TRACK_VALUE = "TRACK_VALUE"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
