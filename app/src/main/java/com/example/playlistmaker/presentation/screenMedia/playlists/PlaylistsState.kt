package com.example.playlistmaker.presentation.screenMedia.playlists

import com.example.playlistmaker.domain.models.Playlist

sealed class PlaylistsState(val isError: Boolean, playlists: List<Playlist>) {

    data class ShowPlaylists(val playlists: List<Playlist>) : PlaylistsState(false, playlists)

    class EmptyPlaylists : PlaylistsState(true, emptyList())
}