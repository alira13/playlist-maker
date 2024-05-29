package com.example.playlistmaker.presentation.editPlaylistScreen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.usecases.playlists.PlaylistInfoInteractor
import com.example.playlistmaker.domain.usecases.playlists.PlaylistsInteractor
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val playlistsInfoInteractor: PlaylistInfoInteractor,
    private val playlist: Playlist
) : ViewModel() {
    fun updatePlaylist(
        playlistName: String,
        playlistDescription: String,
        uri: Uri?
    ) {

        viewModelScope.launch {
            val uriInternalStorage = playlistsInteractor.saveCoverToStorage(uri)

            val newPlaylist = playlist.copy(
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                artworkUrl512 = uriInternalStorage.toString()
            )

            playlistsInfoInteractor.updatePlaylist(newPlaylist)
        }
    }
}
