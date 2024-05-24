package com.example.playlistmaker.presentation.playlistInfo

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.usecases.playlists.PlaylistsInteractor

class PlaylistInfoViewModel(
    private val playlisInteractor: PlaylistsInteractor
) : ViewModel() {
}