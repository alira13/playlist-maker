package com.example.playlistmaker.presentation.mediaScreen.playlists.createPlaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.usecases.playlists.PlaylistsInteractor
import com.example.playlistmaker.presentation.mediaScreen.playlists.PlaylistsState
import com.example.playlistmaker.presentation.models.NewPlaylist

class CreatePlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private var _state = MutableLiveData<PlaylistsState>()
    var state: LiveData<PlaylistsState> = _state

    fun createPlaylist(playlist: NewPlaylist) {
    }
}