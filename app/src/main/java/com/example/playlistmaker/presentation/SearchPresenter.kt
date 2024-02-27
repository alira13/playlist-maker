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

class SearchPresenter(context: Context, private val view: SearchView) {

    private val searchInteractor = Creator.provideSearchInteractor()
    private var searchHistoryInteractor: SearchHistoryInteractor =
        Creator.provideSearchHistoryInteractor(context)

    private val handler: Handler = Handler(Looper.getMainLooper())

    private var lastSearchText: String? = null


    private var searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        search(newSearchText)
    }

    fun searchDebounce(changedText: String) {
        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun search(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            Log.d("MY_LOG", "Start search: $newSearchText")

            view.render(SearchState.Loading)

            searchInteractor.execute(newSearchText,
                consumer = object : Consumer<Track> {
                    override fun consume(data: ConsumerData<Track>) {
                        val currentRunnable = searchRunnable
                        handler.removeCallbacks(currentRunnable)

                        val newDetailsRunnable = Runnable {
                            when (data) {
                                is ConsumerData.NetworkError -> {
                                    view.render(SearchState.ConnectionError)
                                    Log.d("MY_LOG", "CONNECTION ERROR}")
                                }

                                is ConsumerData.EmptyListError -> {
                                    view.render(SearchState.EmptyTrackListError)
                                    Log.d("MY_LOG", "EMPTY LIST ERROR}")
                                }

                                is ConsumerData.Data -> {
                                    val tracks = data.value
                                    view.render(SearchState.TrackList(tracks))
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

    fun addToHistory(track: Track) {
        searchHistoryInteractor.addToHistory(track)
    }

    fun getHistory() = searchHistoryInteractor.getHistory()

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
        view.render(SearchState.EmptyTrackHistory)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}