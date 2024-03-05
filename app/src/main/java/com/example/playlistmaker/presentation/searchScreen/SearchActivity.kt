package com.example.playlistmaker.presentation.searchScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
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
                this,
                SearchViewModel.getViewModelFactory()
            )[SearchViewModel::class.java]

            searchViewModel.observeState().observe(this) {
                render(it)
            }

            binding.backBtn.setOnClickListener {
                finish()
            }

            binding.clearSearchRequestIv.setOnClickListener {
                binding.searchRequestEt.setText("")
                binding.clearSearchRequestIv.visibility = View.GONE
                showTrackHistory()
            }

            trackListRv = findViewById(R.id.track_list_rv)
            trackListRv.apply {
                adapter = trackAdapter
            }

            simpleTextWatcher = object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    Log.d("MY_LOG", "view: beforeTextChanged")
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    Log.d("MY_LOG", "view: onTextChanged")
                    searchViewModel.searchDebounce(changedText = s?.toString() ?: "")
                }

                override fun afterTextChanged(s: Editable?) {
                    Log.d("MY_LOG", "view: afterTextChanged")
                    val inputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                    if (s.isNullOrEmpty()) {
                        inputMethodManager?.hideSoftInputFromWindow(
                            binding.searchRequestEt.windowToken,
                            0
                        )
                        //при пустом поле поиска показываем историю, если она не пустая
                        showTrackHistory()
                    } else {
                        //скрываем историю треков как только начинаем печатать что-то в поиске
                        showEmpty()
                        binding.clearSearchRequestIv.visibility = View.VISIBLE
                        inputMethodManager?.showSoftInput(binding.searchRequestEt, 0)
                    }
                }
            }

            binding.searchRequestEt.addTextChangedListener(simpleTextWatcher)

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
        binding.emptyListErrorTv.visibility = View.GONE
        binding.connectionErrorTv.visibility = View.GONE
        binding.retrySearchBtn.visibility = View.GONE

        binding.trackHistoryTv.visibility = View.GONE
        binding.clearHistoryBtn.visibility = View.GONE
        historyTrackListRv.visibility = View.GONE

        binding.searchPbr.visibility = View.VISIBLE
    }

    private fun showTrackList(tracks: List<Track>) {
        Log.d("MY_LOG", "view: showTrackList")
        binding.trackHistoryTv.visibility = View.GONE
        binding.clearHistoryBtn.visibility = View.GONE
        historyTrackListRv.visibility = View.GONE


        binding.emptyListErrorTv.visibility = View.GONE
        binding.connectionErrorTv.visibility = View.GONE
        binding.retrySearchBtn.visibility = View.GONE

        binding.searchPbr.visibility = View.GONE
        trackListRv.visibility = View.VISIBLE
        trackAdapter.items = tracks.toMutableList()
    }

    private fun showTrackHistory() {
        Log.d("MY_LOG", "view: showTrackHistory")
        binding.emptyListErrorTv.visibility = View.GONE
        binding.connectionErrorTv.visibility = View.GONE
        binding.retrySearchBtn.visibility = View.GONE

        trackListRv.visibility = View.GONE

        if (historyTrackAdapter.items.isNotEmpty()) {
            binding.trackHistoryTv.visibility = View.VISIBLE
            binding.clearHistoryBtn.visibility = View.VISIBLE
            historyTrackListRv.visibility = View.VISIBLE
        }
    }

    private fun showEmptyTrackHistory() {
        Log.d("MY_LOG", "view: showEmptyTrackHistory")
        historyTrackAdapter.clearItems()
        binding.trackHistoryTv.visibility = View.GONE
        binding.clearHistoryBtn.visibility = View.GONE
        historyTrackListRv.visibility = View.GONE
    }

    private fun showConnectionError() {
        Log.d("MY_LOG", "view: showConnectionError")
        binding.searchPbr.visibility = View.GONE
        trackAdapter.clearItems()

        binding.trackHistoryTv.visibility = View.GONE
        binding.clearHistoryBtn.visibility = View.GONE
        historyTrackListRv.visibility = View.GONE

        trackListRv.visibility = View.GONE
        binding.connectionErrorTv.visibility = View.VISIBLE
        binding.retrySearchBtn.visibility = View.VISIBLE
    }

    private fun showEmptyTrackListError() {
        Log.d("MY_LOG", "view: showEmptyTrackListError")
        trackAdapter.clearItems()

        binding.trackHistoryTv.visibility = View.GONE
        binding.clearHistoryBtn.visibility = View.GONE
        historyTrackListRv.visibility = View.GONE

        binding.searchPbr.visibility = View.GONE
        trackListRv.visibility = View.GONE
        binding.emptyListErrorTv.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        Log.d("MY_LOG", "view: showEmpty")
        binding.trackHistoryTv.visibility = View.GONE
        binding.clearHistoryBtn.visibility = View.GONE
        historyTrackListRv.visibility = View.GONE
    }

    companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
        const val TRACK_VALUE = "TRACK_VALUE"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
