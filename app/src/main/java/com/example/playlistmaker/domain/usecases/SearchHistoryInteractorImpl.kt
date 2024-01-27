package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository


class SearchHistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository) :
    SearchHistoryInteractor {

    override fun getHistory(): ArrayList<Track> {
        return searchHistoryRepository.getHistory()
    }

    override fun addToHistory(track: Track) {
        searchHistoryRepository.addToHistory(track)
    }

    override fun clearHistory() {
        searchHistoryRepository.clearHistory()
    }
}