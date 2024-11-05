package com.example.playlistmaker.settings.domain.usecases

import com.example.playlistmaker.settings.domain.models.EmailData

interface ExternalNavigator {
    fun shareLink(link: String)
    fun openLink(link: String)
    fun openEmail(email: EmailData)
    fun sharePlaylist(description: String)
}
