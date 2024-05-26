package com.example.playlistmaker.domain.repository

import android.net.Uri
import com.example.playlistmaker.presentation.models.PlaylistInfo
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun addToPlaylist(playlist: PlaylistInfo)
    fun getPlaylists(): Flow<List<PlaylistInfo>>
    fun saveCoverToStorage(uri: Uri?): Uri
}