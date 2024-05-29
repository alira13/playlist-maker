package com.example.playlistmaker.data.db.playlists

import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.data.converters.TrackDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.favorites.TrackEntity
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.PlaylistInfoRepository
import com.example.playlistmaker.domain.usecases.settings.ExternalNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistInfoRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val trackDbConverter: TrackDbConvertor,
    private val externalNavigator: ExternalNavigator
) : PlaylistInfoRepository {

    override fun getPlaylist(playlistId: Int): Flow<Playlist> = flow {
        val trackEntity = appDatabase.getPlaylistDao().getPlaylist(playlistId)
        val tracks = playlistDbConverter.map(trackEntity)
        emit(tracks)
    }

    override fun getTracks(tracksId: List<Int>): Flow<List<Track>> {
        return flow {
            val allTracks = appDatabase.getPlaylistTracks().getTracks()
            val trackByIds = allTracks.filter { tracksId.contains(it.trackId) }
            val tracks = convertFromTrackEntity(trackByIds)
            emit(tracks)
        }
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
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
            appDatabase.getTrackDao().deleteTrack(trackDbConverter.map(track))
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