package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.Track

interface FavoritesRepository {
    fun addToFavorites(track: Track)
    fun deleteFromRepository(track:Track)
}