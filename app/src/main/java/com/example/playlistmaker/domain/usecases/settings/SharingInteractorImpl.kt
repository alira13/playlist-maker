package com.example.playlistmaker.domain.usecases.settings

import com.example.playlistmaker.domain.models.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        //TODO Нужно реализовать
        return ""
    }

    private fun getSupportEmailData(): EmailData {
        // TODO Нужно реализовать
        return EmailData()
    }

    private fun getTermsLink(): String {
        // TODO Нужно реализовать
        return ""
    }
}