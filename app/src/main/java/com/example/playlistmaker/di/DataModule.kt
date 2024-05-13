package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.app.App.Companion.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.data.externalNavigator.ExternalNavigatorImpl
import com.example.playlistmaker.data.network.TrackRetrofitNetworkClient
import com.example.playlistmaker.data.player.TrackPlayerImpl
import com.example.playlistmaker.data.repository.FavoritesRepositoryImpl
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.repository.SearchRepositoryImpl
import com.example.playlistmaker.data.repository.SettingsRepositoryImpl
import com.example.playlistmaker.data.repository.TrackNetworkClient
import com.example.playlistmaker.data.sharedPreferences.AppSharedPreferences
import com.example.playlistmaker.data.sharedPreferences.AppSharedPreferencesImpl
import com.example.playlistmaker.domain.player.TrackPlayer
import com.example.playlistmaker.domain.repository.FavoritesRepository
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.repository.SearchRepository
import com.example.playlistmaker.domain.repository.SettingsRepository
import com.example.playlistmaker.domain.usecases.settings.ExternalNavigator
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single<TrackPlayer> {
        TrackPlayerImpl()
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }

    single<TrackNetworkClient> {
        TrackRetrofitNetworkClient()
    }

    single<SearchRepository> {
        SearchRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<AppSharedPreferences> {
        AppSharedPreferencesImpl(get(), get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    single {
        androidContext()
            .getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl()
    }

    factory { Gson() }
}