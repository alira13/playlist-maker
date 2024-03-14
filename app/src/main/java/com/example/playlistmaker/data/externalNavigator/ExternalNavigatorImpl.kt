package com.example.playlistmaker.data.externalNavigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.domain.models.EmailData
import com.example.playlistmaker.domain.usecases.settings.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareLink(link: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                link
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun openLink(link: String) {
        val agreementIntent = Intent((Intent.ACTION_VIEW), Uri.parse(link))
        agreementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(agreementIntent)
    }

    override fun openEmail(email: EmailData) {
        val sendIntent = Intent(Intent.ACTION_SENDTO)
        sendIntent.data = Uri.parse("mailto:")
        sendIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email.email))
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, email.emailSubject)
        sendIntent.putExtra(Intent.EXTRA_TEXT, email.message)

        val intent = Intent.createChooser(sendIntent, email.emailSubject)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
