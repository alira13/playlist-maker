package com.example.playlistmaker.presentation.mediaScreen.playlists

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.models.PlaylistInfo

sealed class PlaylistTrackState(val playlist: PlaylistInfo, val track: Track, val message: String) {

    class Exist(playlist: PlaylistInfo, track: Track) :
        PlaylistTrackState(
            playlist,
            track,
            message = "Трек уже добавлен в плейлист ${playlist.playlistName}"
        )

    class NotExist(playlist: PlaylistInfo, track: Track) :
        PlaylistTrackState(
            playlist = playlist,
            track = track,
            message = "Добавлено в плейлист ${playlist.playlistName}"
        )
}