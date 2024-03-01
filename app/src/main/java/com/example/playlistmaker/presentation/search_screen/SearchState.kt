package com.example.playlistmaker.presentation.search_screen

import com.example.playlistmaker.domain.models.Track

sealed interface SearchState {
    object ConnectionError : SearchState
    object EmptyTrackListError : SearchState
    object TrackHistory : SearchState
    data class TrackList(val tracks: List<Track>) : SearchState
    object Loading : SearchState
    object EmptyTrackHistory : SearchState
}