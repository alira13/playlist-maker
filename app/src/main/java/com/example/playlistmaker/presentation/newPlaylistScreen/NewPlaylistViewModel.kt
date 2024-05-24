package com.example.playlistmaker.presentation.newPlaylistScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.usecases.playlists.PlaylistsInteractor
import com.example.playlistmaker.presentation.models.PlaylistInfo
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {
    fun createNewPlayList(
        playlistName: String,
        playlistDescription: String,
        playlistImageName: String
    ) {
        val playListInfo = convertPlaylistInfo(
            playlistName,
            playlistDescription,
            playlistImageName
        )
        viewModelScope.launch {
            playlistsInteractor.addToPlaylist(playListInfo)
        }
    }

    private fun convertPlaylistInfo(
        playlistName: String,
        playlistDescription: String,
        playlistImageName: String
    ) = PlaylistInfo(
        playlistId = 0,
        playlistName = playlistName,
        playlistDescription = playlistDescription,
        artworkUrl512 = playlistImageName,
        trackIds = ArrayList(),
        tracksNum = 0
    )
}
