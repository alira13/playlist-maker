package com.example.playlistmaker.settings.data

import com.example.playlistmaker.core.data.AppSharedPreferences
import com.example.playlistmaker.settings.domain.models.ThemeSettings
import com.example.playlistmaker.settings.domain.repository.SettingsRepository

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