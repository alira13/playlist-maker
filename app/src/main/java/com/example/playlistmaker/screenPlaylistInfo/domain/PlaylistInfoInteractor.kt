package com.example.playlistmaker.screenPlaylistInfo.domain

import com.example.playlistmaker.core.domain.models.Playlist
import com.example.playlistmaker.core.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInfoInteractor {
    fun getPlaylist(playlistId: Int): Flow<Playlist>

    fun getTracks(tracksId: List<Int>): Flow<List<Track>>

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun deleteTrackFromTable(track: Track)

    suspend fun deletePlaylist(playlist: Playlist, tracks: List<Track>)

    fun sharePlaylist(description: String)
}