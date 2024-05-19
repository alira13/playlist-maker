package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.mediaScreen.favorites.MediaFavoritesViewModel
import com.example.playlistmaker.presentation.mediaScreen.playlists.MediaPlaylistsViewModel
import com.example.playlistmaker.presentation.playerScreen.PlayerViewModel
import com.example.playlistmaker.presentation.rootScreen.RootViewModel
import com.example.playlistmaker.presentation.searchScreen.SearchViewModel
import com.example.playlistmaker.presentation.settingsScreen.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<SearchViewModel> {
        SearchViewModel(searchInteractor = get(), searchHistoryInteractor = get())
    }

    viewModel<PlayerViewModel> { params ->
        PlayerViewModel(params.get(), playerInteractor = get())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(settingsInteractor = get(), sharingInteractor = get())
    }

    viewModel<RootViewModel> {
        RootViewModel(settingsInteractor = get())
    }

    viewModel<MediaFavoritesViewModel> {
        MediaFavoritesViewModel(playerInteractor = get())
    }

    viewModel<MediaPlaylistsViewModel> {
        MediaPlaylistsViewModel(playlistsInteractor = get())
    }
}