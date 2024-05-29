package com.example.playlistmaker.domain.models

data class Playlist(
    var playlistId: Int,
    var playlistName: String,
    val playlistDescription: String,
    val tracksNum: Int,
    val artworkUrl512: String,
    var trackIds: List<Int>
)