package com.example.playlistmaker.presentation.search_screen

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecases.search.SearchHistoryInteractor

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val searchInteractor = Creator.provideSearchInteractor()
    private var searchHistoryInteractor: SearchHistoryInteractor =
        Creator.provideSearchHistoryInteractor(getApplication<Application>())

    private val handler: Handler = Handler(Looper.getMainLooper())

    private var lastSearchText: String? = null

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData
    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
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
        searchHistoryInteractor.addToHistory(track)
    }

    fun getHistory() = searchHistoryInteractor.getHistory()

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
        renderState(SearchState.EmptyTrackHistory)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }
}