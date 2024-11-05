package com.example.playlistmaker.settings.presentation

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.models.ThemeSettings
import com.example.playlistmaker.settings.domain.usecases.SettingsInteractor
import com.example.playlistmaker.settings.domain.usecases.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    fun getThemeSettings(): ThemeSettings {
        return settingsInteractor.getTheme()
    }

    fun updateThemeSetting(settings: ThemeSettings) {
        settingsInteractor.updateTheme(settings)
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }
}