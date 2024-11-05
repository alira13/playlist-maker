package com.example.playlistmaker.core.presentation

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.screenSettings.domain.usecases.SettingsInteractor

class MainViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    fun initTheme() {
        settingsInteractor.initTheme()
    }
}