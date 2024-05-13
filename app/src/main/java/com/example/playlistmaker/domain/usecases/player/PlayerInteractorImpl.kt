package com.example.playlistmaker.domain.usecases.player

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerListener
import com.example.playlistmaker.domain.player.TrackPlayer
import com.example.playlistmaker.domain.repository.FavoritesRepository

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

    override fun isLiked(track: Track): Boolean {
        return true
    }

    override fun addToFavorites(track: Track) {
        favoritesRepository.addToFavorites(track)
    }

    override fun deleteFromFavorites(track: Track) {
        favoritesRepository.deleteFromRepository(track)
    }
}