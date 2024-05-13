package com.example.playlistmaker.data.repository

import android.util.Log
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.FavoritesRepository

class FavoritesRepositoryImpl : FavoritesRepository {
    override fun addToFavorites(track: Track) {
        Log.d("MY_LOG", "FavoritesRepositoryImpl addToFavorites")
    }

    override fun deleteFromRepository(track: Track) {
        Log.d("MY_LOG", "FavoritesRepositoryImpl deleteFromRepository")
    }
}