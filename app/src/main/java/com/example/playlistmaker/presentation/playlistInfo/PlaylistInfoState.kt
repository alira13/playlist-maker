package com.example.playlistmaker.presentation.playlistInfo

sealed class PlaylistInfoState {
    data class Empty(val playlistInfo: PlaylistInfo): PlaylistInfoState()

    data class NotEmpty(val playlistInfo: PlaylistInfo): PlaylistInfoState()

    object PlaylistDeleted: PlaylistInfoState()

    data class NoApplicationFound(val feedbackWasShown: Boolean): PlaylistInfoState()

    data class NothingToShare(val feedbackWasShown: Boolean): PlaylistInfoState()
}