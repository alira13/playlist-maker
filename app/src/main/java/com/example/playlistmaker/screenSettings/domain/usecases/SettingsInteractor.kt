package com.example.playlistmaker.screenSettings.domain.usecases

import com.example.playlistmaker.screenSettings.domain.models.ThemeSettings

interface SettingsInteractor {
    fun initTheme()
    fun getTheme(): ThemeSettings
    fun updateTheme(settings: ThemeSettings)
}