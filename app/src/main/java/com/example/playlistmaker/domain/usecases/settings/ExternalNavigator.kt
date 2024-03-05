package com.example.playlistmaker.domain.usecases.settings

import com.example.playlistmaker.domain.models.EmailData

interface ExternalNavigator {
    fun shareLink(link: String)
    fun openLink(link: String)
    fun openEmail(email: EmailData)
}
