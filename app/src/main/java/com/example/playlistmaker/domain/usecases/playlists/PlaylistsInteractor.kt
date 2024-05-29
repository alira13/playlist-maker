package com.example.playlistmaker.domain.usecases.playlists

import android.net.Uri
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun saveCoverToStorage(uri: Uri?): Uri
}