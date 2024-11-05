package com.example.playlistmaker.settings.domain.repository

import com.example.playlistmaker.settings.domain.models.ThemeSettings

interface SettingsRepository {
    fun initTheme()
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}