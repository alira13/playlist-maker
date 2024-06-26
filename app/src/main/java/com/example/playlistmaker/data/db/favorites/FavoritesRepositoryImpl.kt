package com.example.playlistmaker.data.db.favorites

import com.example.playlistmaker.data.converters.TrackDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val movieDbConvertor: TrackDbConvertor,
) : FavoritesRepository {
    override suspend fun addToFavorites(track: Track) {
        val trackEntity = movieDbConvertor.map(track)
        appDatabase.getTrackDao().addTrack(trackEntity)
    }

    override suspend fun deleteFromRepository(track: Track) {
        val trackEntity = movieDbConvertor.map(track)
        appDatabase.getTrackDao().deleteTrack(trackEntity)
    }

    override fun favoritesTracks(): Flow<List<Track>> = flow {
        val trackEntity = appDatabase.getTrackDao().getTracks().reversed()
        val tracks = convertFromTrackEntity(trackEntity).map { it.copy(isFavorite = true) }
        emit(tracks)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> movieDbConvertor.map(track) }
    }
}