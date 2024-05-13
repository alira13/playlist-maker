package com.example.playlistmaker.presentation.searchScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecases.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.usecases.search.SearchInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private var lastSearchText: String? = null
    private var searchJob: Job? = null

    private val _stateLiveData = MutableLiveData<SearchState>()
    val stateLiveData: LiveData<SearchState> = _stateLiveData

    private fun renderState(state: SearchState) {
        Log.d("MY_LOG", "ViewModel:renderState: $state")
        _stateLiveData.postValue(state)
    }

    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText) {
            return
        }
        this.lastSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            search(changedText)
        }
    }

    fun search(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            Log.d("MY_LOG", "Start search: $newSearchText")

            renderState(SearchState.Loading)

            searchJob?.cancel()

            val newJob = viewModelScope.launch {
                searchInteractor
                    .execute(newSearchText)
                    .collect { pair -> processResult(pair.first, pair.second) }
            }
            searchJob = newJob
        }
    }

    private fun processResult(foundTracks: List<Track>?, consumerData: ConsumerData<Track>?) {
        when (consumerData) {
            is ConsumerData.NetworkError -> {
                renderState(SearchState.ConnectionError)
                Log.d("MY_LOG", "CONNECTION ERROR")
            }

            is ConsumerData.EmptyListError -> {
                renderState(SearchState.EmptyTrackListError)
                Log.d("MY_LOG", "EMPTY LIST ERROR")
            }

            is ConsumerData.Data -> {
                val tracks = foundTracks!!
                renderState(SearchState.TrackList(tracks))
                Log.d("MY_LOG", "SUCCESS: $tracks")
            }

            else -> {}
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
    }
}
