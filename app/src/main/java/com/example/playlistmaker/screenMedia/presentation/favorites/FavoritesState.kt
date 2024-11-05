package com.example.playlistmaker.screenMedia.presentation.favorites

import com.example.playlistmaker.core.domain.models.Track

sealed class FavoritesState(val isEmpty: Boolean, val tracks: List<Track>) {

    class NotEmpty(tracks: List<Track>) : FavoritesState(false, tracks)

    class IsEmpty : FavoritesState(true, emptyList())
}