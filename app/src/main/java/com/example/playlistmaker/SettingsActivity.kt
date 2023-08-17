package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val button = findViewById<ImageButton>(R.id.arrow_back_button)
        val buttonClickListener = View.OnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }
        button.setOnClickListener(buttonClickListener)

        val shareButton = findViewById<ImageButton>(R.id.share_button)
        shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.practicum_link))
                type = "text/plain"
            }
            val shareIntent=Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        val supportButton = findViewById<ImageButton>(R.id.support_button)
        supportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            val email=getString(R.string.support_email)
            val emailSubject=getString(R.string.support_email_subject)
            val message =getString(R.string.support_email_message)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            supportIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(supportIntent, emailSubject))
        }

        val agreementButton = findViewById<ImageButton>(R.id.agreement_button)
        val agreementButtonClickListener = View.OnClickListener {
            val web = getString(R.string.agreement_link)
            val agreementIntent=Intent((Intent.ACTION_VIEW), Uri.parse(web))
            startActivity(agreementIntent)
        }
        agreementButton.setOnClickListener(agreementButtonClickListener)
    }
}