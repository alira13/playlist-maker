package com.example.playlistmaker.domain.player

interface PlayerListener {
    fun onCompletion() {}
    fun onPrepare() {}
}