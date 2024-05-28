package com.example.playlistmaker.domain.usecases.playlists

import android.net.Uri
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.PlaylistsRepository
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
) : PlaylistsInteractor {

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistsRepository.createPlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track:Track) {
        playlistsRepository.addToPlaylist(playlist, track)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override suspend fun saveCoverToStorage(uri: Uri?): Uri {
        return playlistsRepository.saveCoverToStorage(uri)
    }
}