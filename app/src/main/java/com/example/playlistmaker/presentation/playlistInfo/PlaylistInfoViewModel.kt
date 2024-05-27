package com.example.playlistmaker.presentation.playlistInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.usecases.playlists.PlaylistInfoInteractor
import com.example.playlistmaker.presentation.models.PlaylistInfo

class PlaylistInfoViewModel(
    private val playlist: PlaylistInfo,
    private val playlistInfoInteractor: PlaylistInfoInteractor
) : ViewModel() {

    private var _state = MutableLiveData<PlaylistInfo>(playlist)
    var state: LiveData<PlaylistInfo> = _state

    fun getTracksByIds() {
        playlistInfoInteractor.getTracksByIds(playlist.trackIds)
    }
}