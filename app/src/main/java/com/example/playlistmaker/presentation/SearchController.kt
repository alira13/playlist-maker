package com.example.playlistmaker.presentation

import android.app.Activity
import android.content.Context
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
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecases.SearchHistoryInteractor
import com.example.playlistmaker.presentation.ui.TrackAdapter

class SearchController(context:Context,
                       private val activity: Activity,
                       private val trackAdapter: TrackAdapter,
                       private val historyTrackAdapter: TrackAdapter) {

    private val searchInteractor = Creator.provideSearchInteractor()
    private var searchHistoryInteractor: SearchHistoryInteractor = Creator.provideSearchHistoryInteractor(context)

    private var searchRunnable = Runnable { search() }
    private val handler: Handler = Handler(Looper.getMainLooper())

    private lateinit var emptyListProblemText: TextView
    private lateinit var searchNoInternetProblemText: TextView
    private lateinit var retrySearchButton: Button
    private lateinit var searchTrackView: EditText
    private lateinit var trackListRecyclerView: RecyclerView

    private lateinit var historyText: TextView
    private lateinit var clearHistoryButton: Button
    private lateinit var historyTrackListRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    fun onCreate(){
        emptyListProblemText = activity.findViewById(R.id.empty_list_problem_text)
        searchNoInternetProblemText = activity.findViewById(R.id.search_no_internet_problem_text)
        val backButton = activity.findViewById<ImageButton>(R.id.arrow_back_button)
        backButton.setOnClickListener {
            activity.finish()
        }

        val clearTrackSearchButton = activity.findViewById<ImageView>(R.id.clear_button)
        clearTrackSearchButton.setOnClickListener {
            searchTrackView.setText("")
            clearTrackSearchButton.visibility = View.GONE
        }

        trackListRecyclerView = activity.findViewById(R.id.track_list)
        trackListRecyclerView.apply {
            //layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = trackAdapter
        }

        searchTrackView = activity.findViewById(R.id.input_edit_text)
        val simpleTextWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int,count: Int,after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //searchValue = s.toString()
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                if (s.isNullOrEmpty()) {
                    clearTrackSearchButton.visibility = View.GONE
                    inputMethodManager?.hideSoftInputFromWindow(searchTrackView.windowToken, 0)
                    //при пустом поле поиска показываем историю, если она не пустая
                    showTrackHistory()
                } else {
                    //скрываем историю треков как только начинаем печатать что-то в поиске
                    hideTrackHistory()
                    clearTrackSearchButton.visibility = View.VISIBLE
                    inputMethodManager?.showSoftInput(searchTrackView, 0)
                }
            }
        }

        searchTrackView.addTextChangedListener(simpleTextWatcher)

        searchTrackView.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus
                && searchTrackView.text.isEmpty()
                && historyTrackAdapter.items.isNotEmpty()
            ) {
                historyTrackAdapter.items = searchHistoryInteractor.getHistory()
                historyTrackAdapter.notifyDataSetChanged()
                showTrackHistory()
            } else {
                showTrackList(historyTrackAdapter.items)
            }
        }

        retrySearchButton = activity.findViewById((R.id.retry_search_button))

        retrySearchButton.setOnClickListener {
            search()
        }

        historyTrackListRecyclerView = activity.findViewById(R.id.history_track_list)
        historyTrackListRecyclerView.apply {
            //layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = historyTrackAdapter
        }

        historyTrackAdapter.items = searchHistoryInteractor.getHistory()

        historyText = activity.findViewById(R.id.history_text)
        clearHistoryButton = activity.findViewById(R.id.clear_history)

        clearHistoryButton.setOnClickListener {
            historyTrackAdapter.clearItems()
            searchHistoryInteractor.clearHistory()
            hideTrackHistory()
        }
        progressBar = activity.findViewById(R.id.progressBar)
    }

    fun addToHistory(track:Track){
        searchHistoryInteractor.addToHistory(track)
        historyTrackAdapter.notifyDataSetChanged()
    }

    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun showTrackHistory() {
        hideTrackList()
        hideErrors()
        if (historyTrackAdapter.items.isNotEmpty()) {
            historyText.visibility = View.VISIBLE
            clearHistoryButton.visibility = View.VISIBLE
            historyTrackListRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun hideTrackHistory() {
        historyText.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE
        historyTrackListRecyclerView.visibility = View.GONE
    }

    fun getSearchText():String{
        return searchTrackView.text.toString()
    }

    fun setSearchText(searchValue:String){
        searchTrackView.setText(searchValue)
    }

    fun showTrackList(tracks: List<Track>) {
        progressBar.visibility = View.GONE
        hideTrackHistory()
        hideErrors()
        trackListRecyclerView.visibility = View.VISIBLE
        trackAdapter.items = tracks.toMutableList()
    }

    private fun search() {
        if (searchTrackView.text.isNotEmpty()) {
            hideErrors()
            progressBar.visibility = View.VISIBLE

            searchInteractor.execute(
                text = searchTrackView.text.toString(),
                consumer = object : Consumer<Track> {
                    override fun consume(data: ConsumerData<Track>) {
                        val currentRunnable = searchRunnable
                        handler.removeCallbacks(currentRunnable)

                        val newDetailsRunnable = Runnable {
                            when (data) {
                                is ConsumerData.NetworkError -> {
                                    showConnectionError()
                                    Log.d("MY_LOG", "CONNECTION ERROR}")
                                }

                                is ConsumerData.EmptyListError -> {
                                    showEmptyTrackListError()
                                    Log.d("MY_LOG", "EMPTY LIST ERROR}")
                                }

                                is ConsumerData.Data -> {
                                    val tracks = data.value
                                    showTrackList(tracks)
                                    Log.d("MY_LOG", "SUCCESS: ${trackAdapter.items}")
                                }
                            }
                        }
                        searchRunnable = newDetailsRunnable
                        handler.post(newDetailsRunnable)
                    }
                }
            )
        }
    }

    private fun showConnectionError() {
        progressBar.visibility = View.GONE
        trackAdapter.clearItems()
        hideTrackHistory()
        hideTrackList()
        searchNoInternetProblemText.visibility = View.VISIBLE
        retrySearchButton.visibility = View.VISIBLE
    }

    private fun showEmptyTrackListError() {
        progressBar.visibility = View.GONE
        trackAdapter.clearItems()
        hideTrackHistory()
        hideTrackList()
        trackListRecyclerView.visibility = View.GONE
        emptyListProblemText.visibility = View.VISIBLE
    }

    private fun hideErrors() {
        emptyListProblemText.visibility = View.GONE
        searchNoInternetProblemText.visibility = View.GONE
        retrySearchButton.visibility = View.GONE
    }

    private fun hideTrackList() {
        trackListRecyclerView.visibility = View.GONE
    }
}