package com.example.playlistmaker.data.db.playlistInfo

import android.app.Application
import com.example.playlistmaker.data.converters.TrackDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.favorites.TrackEntity
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.PlaylistInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistInfoRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
): PlaylistInfoRepository {

    override fun getTracksByIds(ids: List<Int>): Flow<List<Track>> {
        return flow {
            val allTracks = appDatabase.getPlaylistTracks().getTracks()
            val trackByIds = allTracks.filter { ids.contains(it.trackId) }
            val tracks = convertFromTrackEntity(trackByIds)
            emit(tracks)
        }
    }
        private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}