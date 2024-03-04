package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}