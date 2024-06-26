package com.example.playlistmaker.presentation.mediaScreen.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.usecases.playlists.PlaylistsInteractor
import kotlinx.coroutines.launch

class MediaPlaylistsViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private var _state = MutableLiveData<PlaylistsState>()
    var state: LiveData<PlaylistsState> = _state
    fun getState() {
        viewModelScope.launch {
            playlistsInteractor.getPlaylists().collect {
                if (it.isEmpty()) {
                    _state.postValue(PlaylistsState.EmptyPlaylists())
                } else {
                    _state.postValue(PlaylistsState.ShowPlaylists(it))
                }
            }
        }
    }
}