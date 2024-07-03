package com.example.playlistmaker.presentation.mainScreen

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.usecases.settings.SettingsInteractor

class MainViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    fun initTheme() {
        settingsInteractor.initTheme()
    }
}