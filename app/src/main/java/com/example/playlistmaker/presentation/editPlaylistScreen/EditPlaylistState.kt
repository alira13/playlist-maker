package com.example.playlistmaker.presentation.editPlaylistScreen

import com.example.playlistmaker.domain.models.Playlist

sealed class EditPlaylistState {

    data class Current(val playlistInfo: Playlist) : EditPlaylistState()

    data class Edited(val playlistInfo: Playlist) : EditPlaylistState()
}