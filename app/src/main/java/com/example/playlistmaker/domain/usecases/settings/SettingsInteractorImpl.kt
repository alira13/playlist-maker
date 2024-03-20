package com.example.playlistmaker.domain.usecases.settings

import com.example.playlistmaker.domain.models.ThemeSettings
import com.example.playlistmaker.domain.repository.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {
    override fun initTheme() {
        settingsRepository.initTheme()
    }

    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}