package com.example.playlistmaker.presentation.rootScreen

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.usecases.settings.SettingsInteractor

class RootViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    fun initTheme() {
        settingsInteractor.initTheme()
    }
}