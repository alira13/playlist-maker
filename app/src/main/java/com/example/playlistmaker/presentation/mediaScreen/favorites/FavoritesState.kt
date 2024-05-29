package com.example.playlistmaker.presentation.mediaScreen.favorites

import com.example.playlistmaker.domain.models.Track

sealed class FavoritesState(val isEmpty: Boolean, val tracks: List<Track>) {

    class NotEmpty(tracks: List<Track>) : FavoritesState(false, tracks)

    class IsEmpty() : FavoritesState(true, emptyList())
}