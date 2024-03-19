package com.example.playlistmaker.presentation.settingsScreen

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.models.ThemeSettings
import com.example.playlistmaker.domain.usecases.settings.SettingsInteractor
import com.example.playlistmaker.domain.usecases.settings.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    fun getThemeSettings(): ThemeSettings {
        return settingsInteractor.getThemeSettings()
    }

    fun updateThemeSetting(settings: ThemeSettings) {
        settingsInteractor.updateThemeSetting(settings)
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