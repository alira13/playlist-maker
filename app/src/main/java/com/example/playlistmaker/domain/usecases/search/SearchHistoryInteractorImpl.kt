package com.example.playlistmaker.domain.usecases.search

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
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