package com.example.playlistmaker.presentation.settings_screen

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.usecases.settings.SettingsInteractor
import com.example.playlistmaker.domain.usecases.settings.SharingInteractor

class SettingsViewModel(private val sharingInteractor: SharingInteractor,
                        private val settingsInteractor: SettingsInteractor,
    ): ViewModel() {
}