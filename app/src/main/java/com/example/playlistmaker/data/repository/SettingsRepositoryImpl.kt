package com.example.playlistmaker.data.repository

import android.content.Context
import com.example.playlistmaker.data.sharedPreferences.AppSharedPreferencesImpl
import com.example.playlistmaker.domain.models.ThemeSettings
import com.example.playlistmaker.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val applicationContext: Context) : SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        val checked = (applicationContext as AppSharedPreferencesImpl).getNightTheme()
        return ThemeSettings(checked)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        (applicationContext as AppSharedPreferencesImpl).putNightMode(settings.isChecked)
        applicationContext.switchTheme(settings.isChecked)
    }
}