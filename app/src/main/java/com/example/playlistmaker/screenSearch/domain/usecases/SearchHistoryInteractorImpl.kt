package com.example.playlistmaker.screenSearch.domain.usecases

import com.example.playlistmaker.core.domain.models.Track
import com.example.playlistmaker.screenSearch.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow


class SearchHistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository) :
    SearchHistoryInteractor {

    override fun getHistory(): Flow<ArrayList<Track>> {
        return searchHistoryRepository.getHistory()
    }

    override fun addToHistory(track: Track) {
        searchHistoryRepository.addToHistory(track)
    }

    override fun clearHistory() {
        searchHistoryRepository.clearHistory()
    }
}