package com.example.playlistmaker.creator

import com.example.playlistmaker.domain.player.TrackPlayer
import com.example.playlistmaker.data.player.TrackPlayerImpl
import com.example.playlistmaker.domain.usecases.PlayerInteractor
import com.example.playlistmaker.domain.usecases.PlayerInteractorImpl

object Creator {
    private fun getTrackPlayer(): TrackPlayer {
        return TrackPlayerImpl()
    }

    fun provideTrackInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getTrackPlayer())
    }
}