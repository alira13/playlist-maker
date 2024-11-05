package com.example.playlistmaker.settings.domain.usecases

import com.example.playlistmaker.settings.domain.models.ThemeSettings
import com.example.playlistmaker.settings.domain.repository.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {
    override fun initTheme() {
        settingsRepository.initTheme()
    }

    override fun getTheme(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateTheme(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}