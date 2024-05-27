package com.example.playlistmaker.domain.repository

import android.net.Uri
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.models.PlaylistInfo
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun addToPlaylist(playlist: PlaylistInfo, track: Track)
    suspend fun createPlaylist(playlist: PlaylistInfo)
    fun getPlaylists(): Flow<List<PlaylistInfo>>
    fun saveCoverToStorage(uri: Uri?): Uri
}