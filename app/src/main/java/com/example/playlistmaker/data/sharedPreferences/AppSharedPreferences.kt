package com.example.playlistmaker.data.sharedPreferences

import com.example.playlistmaker.domain.models.Track

interface AppSharedPreferences {
    fun switchTheme(nightMode: Boolean)
    fun putNightMode(mode: Boolean)
    fun getNightTheme(): Boolean
    fun putSearchHistory(tracks: ArrayList<Track>)
    fun getSearchHistory(): ArrayList<Track>
}