package com.example.playlistmaker.core.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class AppSharedPreferencesImpl(
    private val sharedPreferences: SharedPreferences,
    private val json: Gson
) :
    AppSharedPreferences {

    override fun initTheme() {
        val nightMode = getNightTheme()
        switchTheme(nightMode)
    }

    override fun switchTheme(nightMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (nightMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferences.edit()
            .putBoolean(NIGHT_MODE, nightMode)
            .apply()
    }

    override fun putNightMode(mode: Boolean) {
        sharedPreferences.edit()
            .putBoolean(NIGHT_MODE, mode)
            .apply()
    }

    override fun getNightTheme(): Boolean {
        return sharedPreferences.getBoolean(NIGHT_MODE, DEFAULT_NIGHT_MODE)
    }

    override fun putSearchHistory(tracks: ArrayList<Track>) {
        val jsonString = json.toJson(tracks)
        sharedPreferences.edit()
            .putString(TRACK_HISTORY, jsonString)
            .apply()
    }

    override fun getSearchHistory(): ArrayList<Track> {
        val jsonString = sharedPreferences.getString(TRACK_HISTORY, null) ?: return ArrayList()
        val token = object : TypeToken<ArrayList<Track>>() {}.type
        return json.fromJson(jsonString, token)
    }

    companion object {
        const val NIGHT_MODE = "night_mode"
        const val DEFAULT_NIGHT_MODE = false
        const val TRACK_HISTORY = "track_history"
    }
}