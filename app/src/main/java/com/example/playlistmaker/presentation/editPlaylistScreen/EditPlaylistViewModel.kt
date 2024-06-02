package com.example.playlistmaker.presentation.editPlaylistScreen

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.usecases.playlists.PlaylistInfoInteractor
import com.example.playlistmaker.domain.usecases.playlists.PlaylistsInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val playlistsInfoInteractor: PlaylistInfoInteractor,
    var playlist: Playlist
) : ViewModel() {

    private val _state = MutableLiveData<EditPlaylistState>()
    val state: LiveData<EditPlaylistState>
        get() = _state

    init {
        getPlaylistCurrentInfo()
    }

    private fun getPlaylistCurrentInfo() {
        viewModelScope.launch {
            playlist = playlistsInfoInteractor.getPlaylist(playlist.playlistId).first()
            _state.postValue(
                EditPlaylistState.Current(playlist)
            )
        }
    }

    fun updatePlaylist(
        playlistName: String,
        playlistDescription: String,
        uri: Uri?
    ) {

        viewModelScope.launch {
            val artworkUrl512: String
            if (uri != null)
                artworkUrl512 = playlistsInteractor.saveCoverToStorage(uri).toString()
            else
                artworkUrl512 = playlist.artworkUrl512

            val newPlaylist = playlist.copy(
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                artworkUrl512 = artworkUrl512
            )

            playlistsInfoInteractor.updatePlaylist(newPlaylist)
            _state.postValue(EditPlaylistState.Edited(newPlaylist))
        }
    }
}
