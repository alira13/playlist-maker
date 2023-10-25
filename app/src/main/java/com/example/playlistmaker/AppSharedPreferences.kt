package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class AppSharedPreferences : Application() {

    private var nightMode = false
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        this.nightMode = sharedPreferences.getBoolean(NIGHT_MODE, this.nightMode)
        switchTheme(nightMode)
    }

    fun switchTheme(nightMode: Boolean) {
        this.nightMode = nightMode
        AppCompatDelegate.setDefaultNightMode(
            if (nightMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferences.edit()
            .putBoolean(NIGHT_MODE, this.nightMode)
            .apply()
    }

    fun putNightMode(mode: Boolean) {
        sharedPreferences.edit()
            .putBoolean(NIGHT_MODE, mode)
            .apply()
    }

    fun getNightTheme(): Boolean {
        return sharedPreferences.getBoolean(NIGHT_MODE, false)
    }

    fun putSearchHistory(tracks: ArrayList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(TRACK_HISTORY, json)
            .apply()
    }

    fun getSearchHistory(): ArrayList<Track> {
        val json = sharedPreferences.getString(TRACK_HISTORY, null) ?: return ArrayList()
        val token = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, token)
    }

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val NIGHT_MODE = "night_mode"
        const val TRACK_HISTORY = "track_history"
    }
}