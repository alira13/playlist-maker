package com.example.playlistmaker.presentation.playlistInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecases.playlists.PlaylistInfoInteractor
import kotlinx.coroutines.launch

class PlaylistInfoViewModel(
    private val playlist: Playlist,
    private val playlistInfoInteractor: PlaylistInfoInteractor
) : ViewModel() {

    private var _state = MutableLiveData<Playlist>(playlist)
    var state: LiveData<Playlist> = _state

    private var _tracksState = MutableLiveData<List<Track>>()
    var tracksState: LiveData<List<Track>> = _tracksState

    fun getTracksByIds() {
        viewModelScope.launch {
            playlistInfoInteractor.getTracksByIds(playlist.trackIds).collect {
                if (it.isEmpty()) {
                    _tracksState.postValue(it)
                }
            }
        }
    }
}