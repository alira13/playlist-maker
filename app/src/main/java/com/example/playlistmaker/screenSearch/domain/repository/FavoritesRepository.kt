package com.example.playlistmaker.screenSearch.domain.repository

import com.example.playlistmaker.core.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun addToFavorites(track: Track)
    suspend fun deleteFromRepository(track: Track)
    fun favoritesTracks(): Flow<List<Track>>
}