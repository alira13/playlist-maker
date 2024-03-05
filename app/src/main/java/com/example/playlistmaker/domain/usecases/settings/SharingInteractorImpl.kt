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
        return "https://practicum.yandex.ru/android-developer/?from=catalog"
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = "Bezglasnaya@gmail.com",
            emailSubject = "Сообщение разработчикам и разработчицам приложения Playlist Maker",
            message = "Спасибо разработчикам и разработчицам за крутое приложение!"
        )
    }

    private fun getTermsLink(): String {
        return "https://yandex.ru/legal/practicum_offer/"
    }
}