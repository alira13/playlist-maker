package com.example.playlistmaker.data.db.playlists

import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.domain.repository.PlaylistsRepository
import com.example.playlistmaker.presentation.models.PlaylistInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConverter,
) : PlaylistsRepository {
    override suspend fun addToPlaylist(playlist: PlaylistInfo) {
        val playlistEntity = playlistDbConvertor.map(playlist)
        appDatabase.getPlaylistDao().addToPlaylists(playlistEntity)
    }

    override fun getPlaylists(): Flow<List<PlaylistInfo>> = flow {
        val trackEntity = appDatabase.getPlaylistDao().getPlaylists().reversed()
        val tracks = convertFromPlaylistEntity(trackEntity)
        emit(tracks)
    }

    private fun convertFromPlaylistEntity(tracks: List<PlaylistEntity>): List<PlaylistInfo> {
        return tracks.map { track -> playlistDbConvertor.map(track) }
    }
}