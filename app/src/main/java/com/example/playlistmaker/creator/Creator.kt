package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.externalNavigator.ExternalNavigatorImpl
import com.example.playlistmaker.data.network.TrackRetrofitNetworkClient
import com.example.playlistmaker.data.player.TrackPlayerImpl
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.repository.SearchRepositoryImpl
import com.example.playlistmaker.data.repository.SettingsRepositoryImpl
import com.example.playlistmaker.data.repository.TrackNetworkClient
import com.example.playlistmaker.domain.player.TrackPlayer
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.repository.SearchRepository
import com.example.playlistmaker.domain.repository.SettingsRepository
import com.example.playlistmaker.domain.usecases.player.PlayerInteractor
import com.example.playlistmaker.domain.usecases.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.usecases.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.usecases.search.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.usecases.search.SearchInteractor
import com.example.playlistmaker.domain.usecases.search.SearchInteractorImpl
import com.example.playlistmaker.domain.usecases.settings.ExternalNavigator
import com.example.playlistmaker.domain.usecases.settings.SettingsInteractor
import com.example.playlistmaker.domain.usecases.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.usecases.settings.SharingInteractor
import com.example.playlistmaker.domain.usecases.settings.SharingInteractorImpl

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

    private fun provideSettingsRepository(applicationContext: Context): SettingsRepository {
        return SettingsRepositoryImpl(applicationContext)
    }

    fun provideSettingsInteractor(applicationContext: Context): SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepository(applicationContext))
    }

    private fun getExternalNavigator(applicationContext: Context): ExternalNavigator {
        return ExternalNavigatorImpl(applicationContext)
    }

    fun provideSharingInteractor(applicationContext: Context): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(applicationContext))
    }
}