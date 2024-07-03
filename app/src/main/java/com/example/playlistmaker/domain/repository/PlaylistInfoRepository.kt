package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInfoRepository {
    fun getPlaylist(playlistId: Int): Flow<Playlist>

    fun getTracks(tracksId: List<Int>): Flow<List<Track>>

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun deleteTrackFromTable(track: Track)

    suspend fun deletePlaylist(playlist: Playlist, tracks: List<Track>)

    fun sharePlaylist(fullPlaylistDesc: String)
}