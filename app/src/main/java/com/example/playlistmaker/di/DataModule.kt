package com.example.playlistmaker.di

import android.content.Context
import androidx.room.Room
import com.example.playlistmaker.app.App.Companion.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.data.converters.TrackDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.favorites.FavoritesRepositoryImpl
import com.example.playlistmaker.data.db.playlists.PlaylistsRepositoryImpl
import com.example.playlistmaker.data.externalNavigator.ExternalNavigatorImpl
import com.example.playlistmaker.data.network.TrackRetrofitNetworkClient
import com.example.playlistmaker.data.player.TrackPlayerImpl
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.repository.SearchRepositoryImpl
import com.example.playlistmaker.data.repository.SettingsRepositoryImpl
import com.example.playlistmaker.data.repository.TrackNetworkClient
import com.example.playlistmaker.data.sharedPreferences.AppSharedPreferences
import com.example.playlistmaker.data.sharedPreferences.AppSharedPreferencesImpl
import com.example.playlistmaker.domain.player.TrackPlayer
import com.example.playlistmaker.domain.repository.FavoritesRepository
import com.example.playlistmaker.domain.repository.PlaylistsRepository
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
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<TrackNetworkClient> {
        TrackRetrofitNetworkClient()
    }

    single<SearchRepository> {
        SearchRepositoryImpl(get(), get())
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
        FavoritesRepositoryImpl(get(), get())
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(get(), get())
    }

    factory { Gson() }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory { TrackDbConvertor() }

    factory { PlaylistDbConverter(json = get()) }
}