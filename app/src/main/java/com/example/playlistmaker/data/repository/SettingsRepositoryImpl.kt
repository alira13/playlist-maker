package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.sharedPreferences.AppSharedPreferences
import com.example.playlistmaker.domain.models.ThemeSettings
import com.example.playlistmaker.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val appSharedPreferences: AppSharedPreferences) :
    SettingsRepository {

    override fun initTheme() {
        appSharedPreferences.initTheme()
    }

    override fun getThemeSettings(): ThemeSettings {
        val checked = appSharedPreferences.getNightTheme()
        return ThemeSettings(checked)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        appSharedPreferences.putNightMode(settings.isChecked)
        appSharedPreferences.switchTheme(settings.isChecked)
    }
}