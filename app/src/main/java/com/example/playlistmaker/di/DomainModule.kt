package com.example.playlistmaker.di

import com.example.playlistmaker.screenPlayer.domain.PlayerInteractor
import com.example.playlistmaker.screenPlayer.domain.PlayerInteractorImpl
import com.example.playlistmaker.screenPlaylistInfo.domain.PlaylistInfoInteractor
import com.example.playlistmaker.screenPlaylistInfo.domain.PlaylistInfoInteractorImpl
import com.example.playlistmaker.core.domain.usecases.PlaylistInteractorImpl
import com.example.playlistmaker.core.domain.usecases.PlaylistsInteractor
import com.example.playlistmaker.screenSearch.domain.usecases.SearchHistoryInteractor
import com.example.playlistmaker.screenSearch.domain.usecases.SearchHistoryInteractorImpl
import com.example.playlistmaker.screenSearch.domain.usecases.SearchInteractor
import com.example.playlistmaker.screenSearch.domain.usecases.SearchInteractorImpl
import com.example.playlistmaker.screenSettings.domain.usecases.SettingsInteractor
import com.example.playlistmaker.screenSettings.domain.usecases.SettingsInteractorImpl
import com.example.playlistmaker.screenSettings.domain.usecases.SharingInteractor
import com.example.playlistmaker.screenSettings.domain.usecases.SharingInteractorImpl
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


