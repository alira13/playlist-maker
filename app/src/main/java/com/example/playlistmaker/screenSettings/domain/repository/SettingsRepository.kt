package com.example.playlistmaker.screenSettings.domain.repository

import com.example.playlistmaker.screenSettings.domain.models.ThemeSettings

interface SettingsRepository {
    fun initTheme()
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}