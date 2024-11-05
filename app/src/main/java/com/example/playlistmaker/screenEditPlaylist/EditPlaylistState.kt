package com.example.playlistmaker.screenEditPlaylist

import com.example.playlistmaker.core.domain.models.Playlist

sealed class EditPlaylistState {

    data class Current(val playlistInfo: Playlist) : EditPlaylistState()

    data class Edited(val playlistInfo: Playlist) : EditPlaylistState()
}