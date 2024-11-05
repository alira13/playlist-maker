package com.example.playlistmaker.core.data

import com.example.playlistmaker.domain.models.Track

interface AppSharedPreferences {
    fun initTheme()
    fun switchTheme(nightMode: Boolean)
    fun putNightMode(mode: Boolean)
    fun getNightTheme(): Boolean
    fun putSearchHistory(tracks: ArrayList<Track>)
    fun getSearchHistory(): ArrayList<Track>
}