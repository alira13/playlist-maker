package com.example.playlistmaker.settings.domain.usecases

import com.example.playlistmaker.settings.domain.models.ThemeSettings

interface SettingsInteractor {
    fun initTheme()
    fun getTheme(): ThemeSettings
    fun updateTheme(settings: ThemeSettings)
}