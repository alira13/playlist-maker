package com.example.playlistmaker.data.db.playlists

import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.data.converters.PlaylistTrackDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.core.domain.models.Playlist
import com.example.playlistmaker.core.domain.models.Track
import com.example.playlistmaker.screenSearch.domain.repository.PlaylistInfoRepository
import com.example.playlistmaker.screenSettings.domain.usecases.ExternalNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistInfoRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val playlistTrackDbConverter: PlaylistTrackDbConvertor,
    private val externalNavigator: ExternalNavigator
) : PlaylistInfoRepository {

    override fun getPlaylist(playlistId: Int): Flow<Playlist> = flow {
        val trackEntity = appDatabase.getPlaylistDao().getPlaylist(playlistId)
        val tracks = playlistDbConverter.map(trackEntity)
        emit(tracks)
    }

    override fun getTracks(tracksId: List<Int>): Flow<List<Track>> {
        return flow {
            val tracks = mutableListOf<Track>()
            for (id in tracksId){
                val playlistTrackEntity = appDatabase.getPlaylistTracks().getTrackById(id)
                tracks.add(playlistTrackDbConverter.map(playlistTrackEntity))
            }
            emit(tracks.reversed())
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.getPlaylistDao().addToPlaylists(
            playlistDbConverter.map(playlist)
        )
    }

    override suspend fun deleteTrackFromTable(track: Track) {
        val playlists = appDatabase.getPlaylistDao().getPlaylists().filter {
            it.trackIds.contains(Regex(track.trackId.toString()))
        }
        if (playlists.isEmpty()) {
            appDatabase.getPlaylistTracks().deleteTrack(playlistTrackDbConverter.map(track))
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist, tracks: List<Track>) {
        appDatabase.getPlaylistDao().deletePlaylist(playlistDbConverter.map(playlist))
        tracks.forEach {
            deleteTrackFromTable(it)
        }
    }

    override fun sharePlaylist(fullPlaylistDesc: String) {
        externalNavigator.sharePlaylist(fullPlaylistDesc)
    }
}