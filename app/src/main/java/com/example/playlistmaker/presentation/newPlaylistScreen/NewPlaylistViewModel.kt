package com.example.playlistmaker.presentation.newPlaylistScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.usecases.playlists.PlaylistsInteractor
import com.example.playlistmaker.presentation.models.NewPlaylist
import com.example.playlistmaker.presentation.models.PlaylistInfo
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {
    fun createNewPlayList(playlist: NewPlaylist) {
        val playListInfo = convertPlaylistInfo(playlist)
        viewModelScope.launch {
            playlistsInteractor.addToPlaylist(playListInfo)
        }
    }

    private fun convertPlaylistInfo(playlist: NewPlaylist) = PlaylistInfo(
        playlistId = 0,
        playlistName = playlist.playlistName,
        playlistDescription = playlist.playlistDescription,
        artworkUrl512 = playlist.artworkUrl512!!,
        trackIds = ArrayList(),
        tracksNum = 0
    )
}