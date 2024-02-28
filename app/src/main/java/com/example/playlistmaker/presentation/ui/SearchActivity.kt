package com.example.playlistmaker.presentation.ui

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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.SearchPresenter
import com.example.playlistmaker.presentation.SearchState
import com.example.playlistmaker.presentation.SearchView
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class SearchActivity :  MvpAppCompatActivity(), ItemClickListener, SearchView  {
    private var searchValue: String = ""

    private val trackAdapter = TrackAdapter(this)
    private val historyTrackAdapter = TrackAdapter(this)

    @InjectPresenter
    lateinit var searchPresenter: SearchPresenter

    @ProvidePresenter
    fun providePresenter(): SearchPresenter {
        return Creator.provideSearchPresenter(
            applicationContext = this.applicationContext
        )
    }

    private var isClickAllowed = true

    private val handler: Handler = Handler(Looper.getMainLooper())

    private var simpleTextWatcher: TextWatcher? = null


    private lateinit var emptyListProblemText: TextView
    private lateinit var searchNoInternetProblemText: TextView
    private lateinit var retrySearchButton: Button
    private lateinit var searchTrackView: EditText
    private lateinit var trackListRecyclerView: RecyclerView

    private lateinit var historyText: TextView
    private lateinit var clearHistoryButton: Button
    private lateinit var historyTrackListRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_search)

            emptyListProblemText = findViewById(R.id.empty_list_problem_text)
            searchNoInternetProblemText = findViewById(R.id.search_no_internet_problem_text)

            val backButton = findViewById<ImageButton>(R.id.arrow_back_button)
            backButton.setOnClickListener {
                finish()
            }

            val clearTrackSearchButton = findViewById<ImageView>(R.id.clear_button)
            clearTrackSearchButton.setOnClickListener {
                setSearchText("")
                clearTrackSearchButton.visibility = View.GONE
                showTrackHistory()
            }

            trackListRecyclerView = findViewById(R.id.track_list)
            trackListRecyclerView.apply {
                adapter = trackAdapter
            }

            searchTrackView = findViewById(R.id.input_edit_text)
            simpleTextWatcher = object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                    Log.d("MY_LOG", "beforeTextChanged")
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    Log.d("MY_LOG", "onTextChanged")
                    searchPresenter.searchDebounce(changedText = s?.toString() ?: "")
                }

                override fun afterTextChanged(s: Editable?) {
                    Log.d("MY_LOG", "afterTextChanged")
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    if (s.isNullOrEmpty()) {
                        inputMethodManager?.hideSoftInputFromWindow(searchTrackView.windowToken, 0)
                        //при пустом поле поиска показываем историю, если она не пустая
                        showTrackHistory()
                    } else {
                        //скрываем историю треков как только начинаем печатать что-то в поиске
                        showEmpty()
                        clearTrackSearchButton.visibility = View.VISIBLE
                        inputMethodManager?.showSoftInput(searchTrackView, 0)
                    }
                }
            }

            searchTrackView.addTextChangedListener(simpleTextWatcher)

            searchTrackView.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && searchTrackView.text.isEmpty() && historyTrackAdapter.items.isNotEmpty()) {
                    historyTrackAdapter.items = searchPresenter.getHistory()
                    historyTrackAdapter.notifyDataSetChanged()
                    showTrackHistory()
                } else {
                    showTrackList(historyTrackAdapter.items)
                }
            }

            retrySearchButton = findViewById((R.id.retry_search_button))

            retrySearchButton.setOnClickListener {
                searchPresenter.search(getSearchText())
            }

            historyTrackListRecyclerView = findViewById(R.id.history_track_list)
            historyTrackListRecyclerView.apply {
                adapter = historyTrackAdapter
            }

            historyTrackAdapter.items = searchPresenter.getHistory()

            historyText = findViewById(R.id.history_text)

            clearHistoryButton = findViewById(R.id.clear_history)
            clearHistoryButton.setOnClickListener {
                searchPresenter.clearHistory()
            }

            progressBar = findViewById(R.id.progressBar)

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
            searchPresenter.addToHistory(track)
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
        simpleTextWatcher?.let { searchTrackView.removeTextChangedListener(it) }
    }

    private fun getSearchText(): String {
        return searchTrackView.text.toString()
    }

    private fun setSearchText(searchValue: String) {
        searchTrackView.setText(searchValue)
    }


    private fun showConnectionError() {
        progressBar.visibility = View.GONE
        trackAdapter.clearItems()

        historyText.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE
        historyTrackListRecyclerView.visibility = View.GONE

        trackListRecyclerView.visibility = View.GONE
        searchNoInternetProblemText.visibility = View.VISIBLE
        retrySearchButton.visibility = View.VISIBLE
    }

    private fun showEmptyTrackListError() {
        trackAdapter.clearItems()

        historyText.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE
        historyTrackListRecyclerView.visibility = View.GONE

        progressBar.visibility = View.GONE
        trackListRecyclerView.visibility = View.GONE
        emptyListProblemText.visibility = View.VISIBLE
    }

    private fun showTrackHistory() {
        Log.d("MY_LOG", "showTrackHistory")
        emptyListProblemText.visibility = View.GONE
        searchNoInternetProblemText.visibility = View.GONE
        retrySearchButton.visibility = View.GONE

        trackListRecyclerView.visibility = View.GONE
        historyText.visibility = View.GONE

        historyText.visibility=View.VISIBLE
        clearHistoryButton.visibility = View.VISIBLE
        historyTrackListRecyclerView.visibility = View.VISIBLE
    }

    private fun showTrackList(tracks: List<Track>) {
        Log.d("MY_LOG", "showTrackList")
        historyText.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE
        historyTrackListRecyclerView.visibility = View.GONE


        emptyListProblemText.visibility = View.GONE
        searchNoInternetProblemText.visibility = View.GONE
        retrySearchButton.visibility = View.GONE

        progressBar.visibility = View.GONE
        trackListRecyclerView.visibility = View.VISIBLE
        trackAdapter.items = tracks.toMutableList()
    }

    private fun showLoading() {
        Log.d("MY_LOG", "showLoading")
        emptyListProblemText.visibility = View.GONE
        searchNoInternetProblemText.visibility = View.GONE
        retrySearchButton.visibility = View.GONE

        historyText.visibility=View.GONE
        clearHistoryButton.visibility = View.GONE
        historyTrackListRecyclerView.visibility = View.GONE

        progressBar.visibility = View.VISIBLE
    }

    private fun showEmptyTrackHistory() {
        Log.d("MY_LOG", "showEmptyTrackHistory")
        historyTrackAdapter.clearItems()
        historyText.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE
        historyTrackListRecyclerView.visibility = View.GONE
    }

    private fun showEmpty() {
        Log.d("MY_LOG", "showEmpty")
        historyText.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE
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
