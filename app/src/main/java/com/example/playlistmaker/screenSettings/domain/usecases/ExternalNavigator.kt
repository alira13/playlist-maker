package com.example.playlistmaker.screenSettings.domain.usecases

import com.example.playlistmaker.screenSettings.domain.models.EmailData

interface ExternalNavigator {
    fun shareLink(link: String)
    fun openLink(link: String)
    fun openEmail(email: EmailData)
    fun sharePlaylist(description: String)
}
