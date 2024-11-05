package com.example.playlistmaker.screenPlayer.domain

import com.example.playlistmaker.core.domain.models.Track
import com.example.playlistmaker.screenSearch.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class PlayerInteractorImpl(
    private val trackPlayer: TrackPlayer,
    private val favoritesRepository: FavoritesRepository
) : PlayerInteractor {

    override fun setListener(listener: PlayerListener) {
        trackPlayer.setListener(listener)
    }

    override fun prepare(trackSource: String) {
        trackPlayer.prepare(trackSource)
    }

    override fun play() {
        trackPlayer.play()
    }

    override fun isPlaying(): Boolean {
        return trackPlayer.isPlaying()
    }

    override fun pause() {
        trackPlayer.pause()
    }

    override fun getCurrentTime(): String {
        return trackPlayer.getCurrentTime()
    }

    override fun quit() {
        trackPlayer.quit()
    }

    override suspend fun addToFavorites(track: Track) {
        favoritesRepository.addToFavorites(track)
    }

    override suspend fun deleteFromFavorites(track: Track) {
        favoritesRepository.deleteFromRepository(track)
    }

    override fun favoritesTracks(): Flow<List<Track>> {
        return favoritesRepository.favoritesTracks()
    }
}