package com.example.playlistmaker.di

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
import com.example.playlistmaker.domain.usecases.settings.ExternalNavigator
import org.koin.dsl.module

val dataModule = module {

    single<TrackPlayer> {
        TrackPlayerImpl()
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(context = get())
    }

    single<TrackNetworkClient> {
        TrackRetrofitNetworkClient()
    }

    single<SearchRepository> {
        SearchRepositoryImpl(trackNetworkClient = get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(applicationContext = get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(context = get())
    }
}