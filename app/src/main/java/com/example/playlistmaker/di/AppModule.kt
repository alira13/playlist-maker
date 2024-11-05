package com.example.playlistmaker.di

import com.example.playlistmaker.screenEditPlaylist.EditPlaylistViewModel
import com.example.playlistmaker.core.presentation.MainViewModel
import com.example.playlistmaker.screenMedia.presentation.favorites.MediaFavoritesViewModel
import com.example.playlistmaker.screenMedia.presentation.playlists.MediaPlaylistsViewModel
import com.example.playlistmaker.screenNewPlaylist.NewPlaylistViewModel
import com.example.playlistmaker.screenPlayer.presentation.PlayerViewModel
import com.example.playlistmaker.screenPlaylistInfo.presentation.PlaylistInfoViewModel
import com.example.playlistmaker.screenSearch.presentation.SearchViewModel
import com.example.playlistmaker.screenSettings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<SearchViewModel> {
        SearchViewModel(searchInteractor = get(), searchHistoryInteractor = get())
    }

    viewModel<PlayerViewModel> { params ->
        PlayerViewModel(params.get(), playerInteractor = get(), playlisInteractor = get())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(settingsInteractor = get(), sharingInteractor = get())
    }

    viewModel<MainViewModel> {
        MainViewModel(settingsInteractor = get())
    }

    viewModel<MediaFavoritesViewModel> {
        MediaFavoritesViewModel(playerInteractor = get())
    }

    viewModel<MediaPlaylistsViewModel> {
        MediaPlaylistsViewModel(playlistsInteractor = get())
    }

    viewModel<NewPlaylistViewModel> {
        NewPlaylistViewModel(playlistsInteractor = get())
    }

    viewModel<PlaylistInfoViewModel> { params ->
        PlaylistInfoViewModel(params.get(), playlistInfoInteractor = get())
    }

    viewModel<EditPlaylistViewModel> { params ->
        EditPlaylistViewModel(get(), get(), params.get())
    }
}