package com.example.playlistmaker.screenMedia.presentation.playlists

import com.example.playlistmaker.core.domain.models.Playlist

sealed class PlaylistsState(val isError: Boolean, playlists: List<Playlist>) {

    data class ShowPlaylists(val playlists: List<Playlist>) : PlaylistsState(false, playlists)

    class EmptyPlaylists : PlaylistsState(true, emptyList())
}