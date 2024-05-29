package com.example.playlistmaker.presentation.models

data class NewPlaylist(
    var playlistName: String,
    val playlistDescription: String = "",
    val artworkUrl512: String? = "",
    val trackNum: Int = 0
)