package com.example.playlistmaker.domain.repository

import android.net.Uri
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun addToPlaylist(playlist: Playlist, track: Track)
    suspend fun createPlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    fun saveCoverToStorage(uri: Uri?): Uri
}