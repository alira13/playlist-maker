package com.example.playlistmaker.presentation.mediaScreen.playlists

import com.example.playlistmaker.presentation.models.PlaylistInfo

sealed class PlaylistsState(val isError: Boolean, playlists: List<PlaylistInfo>) {

    data class ShowPlaylists(val playlists: List<PlaylistInfo>) : PlaylistsState(false, playlists)

    class EmptyPlaylists : PlaylistsState(true, emptyList())
}