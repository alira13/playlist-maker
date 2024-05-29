package com.example.playlistmaker.presentation.playlistInfo

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track

class PlaylistInfo(val playlist: Playlist, val tracks: List<Track>?) {
}