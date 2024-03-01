package com.example.playlistmaker.presentation.search_screen

import android.annotation.SuppressLint
import android.content.Context
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
import com.example.playlistmaker.presentation.ui.ItemClickListener
import com.example.playlistmaker.presentation.player_screen.PlayerActivity
import com.example.playlistmaker.presentation.ui.TrackAdapter

class SearchActivity : AppCompatActivity(), ItemClickListener, SearchView {

    private lateinit var binding: ActivitySearchBinding
    lateinit var searchViewModel: SearchViewModel


    private var searchValue: String = ""
    private var isClickAllowed = true

    private val trackAdapter = TrackAdapter(this)
    private val historyTrackAdapter = TrackAdapter(this)

    private val handler: Handler = Handler(Looper.getMainLooper())

    private var simpleTextWatcher: TextWatcher? = null

    private lateinit var trackListRecyclerView: RecyclerView
    private lateinit var historyTrackListRecyclerView: RecyclerView

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
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

            binding.arrowBackButton.setOnClickListener {
                finish()
            }

            binding.clearButton.setOnClickListener {
                setSearchText("")
                binding.clearButton.visibility = View.GONE
                showTrackHistory()
            }

            trackListRecyclerView = findViewById(R.id.track_list)
            trackListRecyclerView.apply {
                adapter = trackAdapter
            }

            simpleTextWatcher = object : TextWatcher {

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    Log.d("MY_LOG", "beforeTextChanged")
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    Log.d("MY_LOG", "onTextChanged")
                    searchViewModel.searchDebounce(changedText = s?.toString() ?: "")
                }

                override fun afterTextChanged(s: Editable?) {
                    Log.d("MY_LOG", "afterTextChanged")
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    if (s.isNullOrEmpty()) {
                        inputMethodManager?.hideSoftInputFromWindow(
                            binding.inputEditText.windowToken,
                            0
                        )
                        //при пустом поле поиска показываем историю, если она не пустая
                        showTrackHistory()
                    } else {
                        //скрываем историю треков как только начинаем печатать что-то в поиске
                        showEmpty()
                        binding.clearButton.visibility = View.VISIBLE
                        inputMethodManager?.showSoftInput(binding.inputEditText, 0)
                    }
                }
            }

            binding.inputEditText.addTextChangedListener(simpleTextWatcher)

            binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && binding.inputEditText.text.isEmpty() && historyTrackAdapter.items.isNotEmpty()) {
                    historyTrackAdapter.items = searchViewModel.getHistory()
                    historyTrackAdapter.notifyDataSetChanged()
                    showTrackHistory()
                } else {
                    showTrackList(historyTrackAdapter.items)
                }
            }

            binding.retrySearchButton.setOnClickListener {
                searchViewModel.search(getSearchText())
            }

            historyTrackListRecyclerView = findViewById(R.id.history_track_list)
            historyTrackListRecyclerView.apply {
                adapter = historyTrackAdapter
            }

            historyTrackAdapter.items = searchViewModel.getHistory()

            binding.clearHistory.setOnClickListener {
                searchViewModel.clearHistory()
            }

        } catch (ex: Exception) {
            Log.d("MY", ex.message.toString(), ex.fillInStackTrace())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, getSearchText())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchValue = savedInstanceState.getString(SEARCH_VALUE, "")
        setSearchText(searchValue)
    }


    override fun onClick(track: Track) {
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
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleTextWatcher?.let { binding.inputEditText.removeTextChangedListener(it) }
    }

    private fun getSearchText(): String {
        return binding.inputEditText.text.toString()
    }

    private fun setSearchText(searchValue: String) {
        binding.inputEditText.setText(searchValue)
    }


    private fun showConnectionError() {
        binding.progressBar.visibility = View.GONE
        trackAdapter.clearItems()

        binding.historyText.visibility = View.GONE
        binding.clearHistory.visibility = View.GONE
        historyTrackListRecyclerView.visibility = View.GONE

        trackListRecyclerView.visibility = View.GONE
        binding.searchNoInternetProblemText.visibility = View.VISIBLE
        binding.retrySearchButton.visibility = View.VISIBLE
    }

    private fun showEmptyTrackListError() {
        trackAdapter.clearItems()

        binding.historyText.visibility = View.GONE
        binding.clearHistory.visibility = View.GONE
        historyTrackListRecyclerView.visibility = View.GONE

        binding.progressBar.visibility = View.GONE
        trackListRecyclerView.visibility = View.GONE
        binding.emptyListProblemText.visibility = View.VISIBLE
    }

    private fun showTrackHistory() {
        Log.d("MY_LOG", "showTrackHistory")
        binding.emptyListProblemText.visibility = View.GONE
        binding.searchNoInternetProblemText.visibility = View.GONE
        binding.retrySearchButton.visibility = View.GONE

        trackListRecyclerView.visibility = View.GONE

        binding.historyText.visibility = View.VISIBLE
        binding.clearHistory.visibility = View.VISIBLE
        historyTrackListRecyclerView.visibility = View.VISIBLE
    }

    private fun showTrackList(tracks: List<Track>) {
        Log.d("MY_LOG", "showTrackList")
        binding.historyText.visibility = View.GONE
        binding.clearHistory.visibility = View.GONE
        historyTrackListRecyclerView.visibility = View.GONE


        binding.emptyListProblemText.visibility = View.GONE
        binding.searchNoInternetProblemText.visibility = View.GONE
        binding.retrySearchButton.visibility = View.GONE

        binding.progressBar.visibility = View.GONE
        trackListRecyclerView.visibility = View.VISIBLE
        trackAdapter.items = tracks.toMutableList()
    }

    private fun showLoading() {
        Log.d("MY_LOG", "showLoading")
        binding.emptyListProblemText.visibility = View.GONE
        binding.searchNoInternetProblemText.visibility = View.GONE
        binding.retrySearchButton.visibility = View.GONE

        binding.historyText.visibility = View.GONE
        binding.clearHistory.visibility = View.GONE
        historyTrackListRecyclerView.visibility = View.GONE

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showEmptyTrackHistory() {
        Log.d("MY_LOG", "showEmptyTrackHistory")
        historyTrackAdapter.clearItems()
        binding.historyText.visibility = View.GONE
        binding.clearHistory.visibility = View.GONE
        historyTrackListRecyclerView.visibility = View.GONE
    }

    private fun showEmpty() {
        Log.d("MY_LOG", "showEmpty")
        binding.historyText.visibility = View.GONE
        binding.clearHistory.visibility = View.GONE
        historyTrackListRecyclerView.visibility = View.GONE
    }

    override fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.TrackList -> showTrackList(state.tracks)
            is SearchState.TrackHistory -> showTrackHistory()
            is SearchState.EmptyTrackHistory -> showEmptyTrackHistory()
            is SearchState.ConnectionError -> showConnectionError()
            is SearchState.EmptyTrackListError -> showEmptyTrackListError()
        }
    }

    companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
        const val TRACK_VALUE = "TRACK_VALUE"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
