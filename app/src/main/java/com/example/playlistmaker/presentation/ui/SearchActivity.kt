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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.SearchPresenter
import com.example.playlistmaker.presentation.SearchView

class SearchActivity : AppCompatActivity(), ItemClickListener, SearchView {
    private var searchValue: String = ""

    private val trackAdapter = TrackAdapter(this)
    private val historyTrackAdapter = TrackAdapter(this)

    private lateinit var searchPresenter: SearchPresenter

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

            searchPresenter = Creator.provideSearchPresenter(
                applicationContext, this
            )
            //searchPresenter.onCreate()

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
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    searchPresenter.searchDebounce(changedText = s?.toString() ?: "")
                }

                override fun afterTextChanged(s: Editable?) {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    if (s.isNullOrEmpty()) {
                        clearTrackSearchButton.visibility = View.GONE
                        inputMethodManager?.hideSoftInputFromWindow(searchTrackView.windowToken, 0)
                        //при пустом поле поиска показываем историю, если она не пустая
                        searchPresenter.showTrackHistory()
                    } else {
                        //скрываем историю треков как только начинаем печатать что-то в поиске
                        searchPresenter.hideTrackHistory()
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
                    searchPresenter.showTrackHistory()
                } else {
                    searchPresenter.showTrackList(historyTrackAdapter.items)
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

    companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
        const val TRACK_VALUE = "TRACK_VALUE"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleTextWatcher?.let { searchTrackView.removeTextChangedListener(it) }
    }

    override fun showHistoryText(isVisible: Boolean) {
        if (isVisible && historyTrackAdapter.items.isNotEmpty()) historyText.visibility =
            View.VISIBLE
        else historyText.visibility = View.GONE
    }

    override fun showClearHistoryButton(isVisible: Boolean) {
        if (isVisible && historyTrackAdapter.items.isNotEmpty()) clearHistoryButton.visibility =
            View.VISIBLE
        else clearHistoryButton.visibility = View.GONE
    }

    override fun showHistoryTrackListRecyclerView(isVisible: Boolean) {
        if (isVisible && historyTrackAdapter.items.isNotEmpty()) historyTrackListRecyclerView.visibility =
            View.VISIBLE
        else historyTrackListRecyclerView.visibility = View.GONE
    }

    override fun showProgressBar(isVisible: Boolean) {
        if (isVisible) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

    override fun showSearchNoInternetProblemText(isVisible: Boolean) {
        if (isVisible) searchNoInternetProblemText.visibility = View.VISIBLE
        else searchNoInternetProblemText.visibility = View.GONE
    }

    override fun showRetrySearchButton(isVisible: Boolean) {
        if (isVisible) retrySearchButton.visibility = View.VISIBLE
        else retrySearchButton.visibility = View.GONE
    }

    override fun showEmptyListProblemText(isVisible: Boolean) {
        if (isVisible) emptyListProblemText.visibility = View.VISIBLE
        else emptyListProblemText.visibility = View.GONE
    }

    override fun showTrackListRecyclerView(isVisible: Boolean) {
        if (isVisible) trackListRecyclerView.visibility = View.VISIBLE
        else trackListRecyclerView.visibility = View.GONE
    }

    override fun getSearchText(): String {
        return searchTrackView.text.toString()
    }

    override fun setSearchText(searchValue: String) {
        searchTrackView.setText(searchValue)
    }

    override fun updateTrackList(tracks: List<Track>) {
        trackAdapter.items = tracks.toMutableList()
    }

    override fun clearTrackList() {
        trackAdapter.clearItems()
    }

    override fun updateHistoryTrackList() {
        historyTrackAdapter.notifyDataSetChanged()
    }

    override fun clearHistoryTrackList() {
        historyTrackAdapter.clearItems()
    }
}
