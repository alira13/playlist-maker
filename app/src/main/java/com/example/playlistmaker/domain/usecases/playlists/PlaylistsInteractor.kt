package com.example.playlistmaker.domain.usecases.playlists

import android.net.Uri
import com.example.playlistmaker.presentation.models.PlaylistInfo
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun addToPlaylist(playlist: PlaylistInfo)
    fun getPlaylists(): Flow<List<PlaylistInfo>>
    suspend fun saveCoverToStorage(uri: Uri?): Uri
}