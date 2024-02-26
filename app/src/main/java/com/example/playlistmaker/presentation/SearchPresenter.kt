package com.example.playlistmaker.presentation

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecases.SearchHistoryInteractor

class SearchPresenter(
    context: Context, private val view: SearchView
) {

    private val searchInteractor = Creator.provideSearchInteractor()
    private var searchHistoryInteractor: SearchHistoryInteractor =
        Creator.provideSearchHistoryInteractor(context)

    private val handler: Handler = Handler(Looper.getMainLooper())

    private var lastSearchText: String? = null

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    fun addToHistory(track: Track) {
        searchHistoryInteractor.addToHistory(track)
        view.updateHistoryTrackList()
    }

    fun searchDebounce(changedText: String) {
        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private var searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        search(newSearchText)
    }

    fun search(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            Log.d("MY_LOG", "Start search: $newSearchText")
            hideErrors()
            view.showProgressBar(true)

            searchInteractor.execute(
                text = view.getSearchText(),
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
                                    Log.d("MY_LOG", "SUCCESS: $tracks")
                                }
                            }
                        }
                        searchRunnable = newDetailsRunnable
                        handler.post(newDetailsRunnable)
                    }
                })
        }
    }

    fun showTrackHistory() {
        hideErrors()
        view.showTrackListRecyclerView(false)
        view.showHistoryText(true)
        view.showClearHistoryButton(true)
        view.showHistoryTrackListRecyclerView(true)
    }

    fun hideTrackHistory() {
        view.showHistoryText(false)
        view.showClearHistoryButton(false)
        view.showHistoryTrackListRecyclerView(false)
    }

    fun showTrackList(tracks: List<Track>) {
        hideTrackHistory()
        hideErrors()
        view.showProgressBar(false)
        view.showTrackListRecyclerView(true)
        view.updateTrackList(tracks)
    }

    private fun showConnectionError() {
        view.showProgressBar(false)
        view.clearTrackList()
        hideTrackHistory()
        view.showTrackListRecyclerView(false)
        view.showSearchNoInternetProblemText(true)
        view.showRetrySearchButton(true)
    }

    private fun showEmptyTrackListError() {
        view.clearTrackList()
        hideTrackHistory()
        view.showProgressBar(false)
        view.showTrackListRecyclerView(false)
        view.showEmptyListProblemText(true)
    }

    private fun hideErrors() {
        view.showEmptyListProblemText(false)
        view.showSearchNoInternetProblemText(false)
        view.showRetrySearchButton(false)
    }

    fun getHistory() = searchHistoryInteractor.getHistory()

    fun clearHistory() {
        view.clearHistoryTrackList()
        searchHistoryInteractor.clearHistory()
        hideTrackHistory()
    }
}