package com.example.playlistmaker.screenSearch.domain.repository

import android.net.Uri
import com.example.playlistmaker.core.domain.models.Playlist
import com.example.playlistmaker.core.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun addToPlaylist(playlist: Playlist, track: Track)
    suspend fun createPlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    fun saveCoverToStorage(uri: Uri?): Uri
}