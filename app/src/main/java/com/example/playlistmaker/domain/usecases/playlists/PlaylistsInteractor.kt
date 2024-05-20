package com.example.playlistmaker.domain.usecases.playlists

import com.example.playlistmaker.presentation.models.PlaylistInfo
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun addToPlaylists(track: PlaylistInfo)
    fun getPlaylists(): Flow<List<PlaylistInfo>>
}