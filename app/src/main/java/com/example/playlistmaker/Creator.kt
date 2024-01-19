package com.example.playlistmaker

import com.example.playlistmaker.data.TrackPlayer
import com.example.playlistmaker.data.TrackPlayerImpl
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