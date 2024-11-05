package com.example.playlistmaker.screenPlayer.domain

interface PlayerListener {
    fun onCompletion() {}
    fun onPrepare() {}
}