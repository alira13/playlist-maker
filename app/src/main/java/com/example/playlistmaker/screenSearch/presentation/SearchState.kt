package com.example.playlistmaker.screenSearch.presentation

import com.example.playlistmaker.core.domain.models.Track

sealed interface SearchState {
    object ConnectionError : SearchState
    object EmptyTrackListError : SearchState
    data class TrackHistory(val tracks: List<Track>) : SearchState
    data class TrackList(val tracks: List<Track>) : SearchState
    object Loading : SearchState
    object EmptyTrackHistory : SearchState
}