package com.example.playlistmaker.domain.usecases.playlists

import android.net.Uri
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.models.PlaylistInfo
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun createPlaylist(playlist: PlaylistInfo)
    suspend fun addTrackToPlaylist(playlist: PlaylistInfo, track: Track)
    fun getPlaylists(): Flow<List<PlaylistInfo>>
    suspend fun saveCoverToStorage(uri: Uri?): Uri
}