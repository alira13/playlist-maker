package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.domain.player.TrackPlayer
import com.example.playlistmaker.data.player.TrackPlayerImpl
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.usecases.PlayerInteractor
import com.example.playlistmaker.domain.usecases.PlayerInteractorImpl
import com.example.playlistmaker.domain.usecases.SearchHistoryInteractor
import com.example.playlistmaker.domain.usecases.SearchHistoryInteractorImpl

object Creator {
    private fun getTrackPlayer(): TrackPlayer {
        return TrackPlayerImpl()
    }

    fun provideTrackInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getTrackPlayer())
    }

    private fun provideSearchHistoryRepository(applicationContext: Context):SearchHistoryRepository{
        return  SearchHistoryRepositoryImpl(applicationContext)
    }

    fun provideSearchHistoryInteractor(applicationContext: Context):SearchHistoryInteractor{
        SearchHistoryRepositoryImpl(applicationContext)
        return  SearchHistoryInteractorImpl(provideSearchHistoryRepository(applicationContext))
    }
}