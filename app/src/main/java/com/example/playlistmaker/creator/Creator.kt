package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.network.TrackRetrofitNetworkClient
import com.example.playlistmaker.data.player.TrackPlayerImpl
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.repository.SearchRepositoryImpl
import com.example.playlistmaker.data.repository.TrackNetworkClient
import com.example.playlistmaker.domain.player.TrackPlayer
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.repository.SearchRepository
import com.example.playlistmaker.domain.usecases.player.PlayerInteractor
import com.example.playlistmaker.domain.usecases.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.usecases.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.usecases.search.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.usecases.search.SearchInteractor
import com.example.playlistmaker.domain.usecases.search.SearchInteractorImpl

object Creator {
    private fun getTrackPlayer(): TrackPlayer {
        return TrackPlayerImpl()
    }

    fun provideTrackInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getTrackPlayer())
    }

    private fun provideSearchHistoryRepository(applicationContext: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(applicationContext)
    }

    fun provideSearchHistoryInteractor(applicationContext: Context): SearchHistoryInteractor {
        SearchHistoryRepositoryImpl(applicationContext)
        return SearchHistoryInteractorImpl(provideSearchHistoryRepository(applicationContext))
    }

    private fun getNetworkClient(): TrackNetworkClient {
        return TrackRetrofitNetworkClient()
    }

    private fun provideSearchRepository(): SearchRepository {
        return SearchRepositoryImpl(getNetworkClient())
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(provideSearchRepository())
    }
}