package com.example.playlistmaker.domain.usecases.playlists

import android.net.Uri
import com.example.playlistmaker.domain.repository.PlaylistsRepository
import com.example.playlistmaker.presentation.models.PlaylistInfo
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
) : PlaylistsInteractor {

    override suspend fun addToPlaylist(playlist: PlaylistInfo) {
        playlistsRepository.addToPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<PlaylistInfo>> {
        return playlistsRepository.getPlaylists()
    }

    override suspend fun saveCoverToStorage(uri: Uri?): Uri {
        return playlistsRepository.saveCoverToStorage(uri)
    }
}