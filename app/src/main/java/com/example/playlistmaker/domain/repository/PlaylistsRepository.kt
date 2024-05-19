package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.presentation.models.PlaylistInfo
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun addToPlaylists(track: PlaylistInfo)
    fun getPlaylists(): Flow<List<PlaylistInfo>>
}