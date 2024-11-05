package com.example.playlistmaker.screenSettings.domain.usecases

import com.example.playlistmaker.screenSettings.domain.models.ThemeSettings
import com.example.playlistmaker.screenSettings.domain.repository.SettingsRepository

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