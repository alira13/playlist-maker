package com.example.playlistmaker.screenSettings.presentation

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.screenSettings.domain.models.ThemeSettings
import com.example.playlistmaker.screenSettings.domain.usecases.SettingsInteractor
import com.example.playlistmaker.screenSettings.domain.usecases.SharingInteractor

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