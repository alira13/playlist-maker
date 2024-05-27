package com.example.playlistmaker.presentation.playlistInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecases.playlists.PlaylistInfoInteractor
import com.example.playlistmaker.presentation.models.PlaylistInfo
import kotlinx.coroutines.launch

class PlaylistInfoViewModel(
    private val playlist: PlaylistInfo,
    private val playlistInfoInteractor: PlaylistInfoInteractor
) : ViewModel() {

    private var _state = MutableLiveData<PlaylistInfo>(playlist)
    var state: LiveData<PlaylistInfo> = _state

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