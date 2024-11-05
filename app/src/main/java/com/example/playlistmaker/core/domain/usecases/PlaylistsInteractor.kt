package com.example.playlistmaker.core.domain.usecases

import android.net.Uri
import com.example.playlistmaker.core.domain.models.Playlist
import com.example.playlistmaker.core.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun saveCoverToStorage(uri: Uri?): Uri
}