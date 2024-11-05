package com.example.playlistmaker.screenMedia.presentation.playlists

import com.example.playlistmaker.core.domain.models.Playlist
import com.example.playlistmaker.core.domain.models.Track

sealed class PlaylistTrackState(val playlist: Playlist, val track: Track, val message: String) {

    class Exist(playlist: Playlist, track: Track) :
        PlaylistTrackState(
            playlist,
            track,
            message = "Трек уже добавлен в плейлист ${playlist.playlistName}"
        )

    class NotExist(playlist: Playlist, track: Track) :
        PlaylistTrackState(
            playlist = playlist,
            track = track,
            message = "Добавлено в плейлист ${playlist.playlistName}"
        )
}