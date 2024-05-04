package com.example.playlistmaker.presentation.searchScreen

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecases.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.usecases.search.SearchInteractor

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val handler: Handler = Handler(Looper.getMainLooper())

    private var lastSearchText: String? = null

    private val _stateLiveData = MutableLiveData<SearchState>()
    val stateLiveData: LiveData<SearchState> = _stateLiveData

    private fun renderState(state: SearchState) {
        Log.d("MY_LOG", "ViewModel:renderState: $state")
        _stateLiveData.postValue(state)
    }

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

            renderState(SearchState.Loading)

            searchInteractor.execute(newSearchText,
                consumer = object : Consumer<Track> {
                    override fun consume(data: ConsumerData<Track>) {
                        val currentRunnable = searchRunnable
                        handler.removeCallbacks(currentRunnable)

                        val newDetailsRunnable = Runnable {
                            when (data) {
                                is ConsumerData.NetworkError -> {
                                    renderState(SearchState.ConnectionError)
                                }

                                is ConsumerData.EmptyListError -> {
                                    renderState(SearchState.EmptyTrackListError)
                                    Log.d("MY_LOG", "EMPTY LIST ERROR")
                                }

                                is ConsumerData.Data -> {
                                    val tracks = data.value
                                    renderState(SearchState.TrackList(tracks))
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
        Log.d("MY_LOG", "ADD TO HISTORY")
        searchHistoryInteractor.addToHistory(track)
    }

    fun getHistory() {
        Log.d("MY_LOG", "GET HISTORY")
        val tracks = searchHistoryInteractor.getHistory()
        renderState(SearchState.TrackHistory(tracks))
    }

    fun clearHistory() {
        Log.d("MY_LOG", "CLEAR HISTORY")
        searchHistoryInteractor.clearHistory()
        renderState(SearchState.EmptyTrackHistory)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }
}