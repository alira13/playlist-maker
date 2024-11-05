package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.screenEditPlaylist.EditPlaylistViewModel
import com.example.playlistmaker.presentation.screenMain.MainViewModel
import com.example.playlistmaker.presentation.screenMedia.favorites.MediaFavoritesViewModel
import com.example.playlistmaker.presentation.screenMedia.playlists.MediaPlaylistsViewModel
import com.example.playlistmaker.presentation.screenNewPlaylist.NewPlaylistViewModel
import com.example.playlistmaker.presentation.screenPlayer.PlayerViewModel
import com.example.playlistmaker.presentation.screenPlaylistInfo.PlaylistInfoViewModel
import com.example.playlistmaker.presentation.screenSearch.SearchViewModel
import com.example.playlistmaker.presentation.screenSettings.SettingsViewModel
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