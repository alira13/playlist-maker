package com.example.playlistmaker.presentation

import com.example.playlistmaker.domain.models.Track

interface SearchView {
    fun showHistoryText(isVisible: Boolean)
    fun showClearHistoryButton(isVisible: Boolean)
    fun showHistoryTrackListRecyclerView(isVisible: Boolean)
    fun showProgressBar(isVisible: Boolean)
    fun showSearchNoInternetProblemText(isVisible: Boolean)
    fun showRetrySearchButton(isVisible: Boolean)
    fun showEmptyListProblemText(isVisible: Boolean)
    fun showTrackListRecyclerView(isVisible: Boolean)
    fun getSearchText(): String
    fun setSearchText(searchValue: String)
    fun updateTrackList(tracks: List<Track>)
    fun clearTrackList()
    fun updateHistoryTrackList()
    fun clearHistoryTrackList()
}