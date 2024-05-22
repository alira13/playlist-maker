package com.example.playlistmaker.presentation.models

data class PlaylistInfo(
    var playlistId: Int,
    var playlistName: String,
    val playlistDescription: String,
    val tracksNum: Int,
    val artworkUrl512: String,
    var trackIds: List<Int>
)