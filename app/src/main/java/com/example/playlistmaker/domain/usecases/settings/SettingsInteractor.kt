package com.example.playlistmaker.domain.usecases.settings

import com.example.playlistmaker.domain.models.ThemeSettings

interface SettingsInteractor {
    fun initTheme()
    fun getTheme(): ThemeSettings
    fun updateTheme(settings: ThemeSettings)
}