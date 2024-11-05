package com.example.playlistmaker.di

import com.example.playlistmaker.domain.usecases.player.PlayerInteractor
import com.example.playlistmaker.domain.usecases.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.usecases.playlists.PlaylistInfoInteractor
import com.example.playlistmaker.domain.usecases.playlists.PlaylistInfoInteractorImpl
import com.example.playlistmaker.domain.usecases.playlists.PlaylistInteractorImpl
import com.example.playlistmaker.domain.usecases.playlists.PlaylistsInteractor
import com.example.playlistmaker.domain.usecases.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.usecases.search.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.usecases.search.SearchInteractor
import com.example.playlistmaker.domain.usecases.search.SearchInteractorImpl
import com.example.playlistmaker.domain.usecases.settings.SettingsInteractor
import com.example.playlistmaker.domain.usecases.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.usecases.settings.SharingInteractor
import com.example.playlistmaker.domain.usecases.settings.SharingInteractorImpl
import org.koin.dsl.module

val domainModule = module {
    factory<SharingInteractor> {
        SharingInteractorImpl(externalNavigator = get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(settingsRepository = get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(trackRepository = get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(searchHistoryRepository = get())
    }

    factory<PlayerInteractor> {
        PlayerInteractorImpl(trackPlayer = get(), favoritesRepository = get())
    }

    factory<PlaylistsInteractor> {
        PlaylistInteractorImpl(playlistsRepository = get())
    }

    factory<PlaylistInfoInteractor> {
        PlaylistInfoInteractorImpl(playlistInfoRepository = get())
    }
}


