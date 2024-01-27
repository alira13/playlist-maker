package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.data.sharedPreferences.AppSharedPreferencesImpl

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val nightModeSwitch = findViewById<Switch>(R.id.night_mode_switch)
        nightModeSwitch.isChecked = (applicationContext as AppSharedPreferencesImpl).getNightTheme()
        nightModeSwitch.setOnCheckedChangeListener { _, checked ->
            (applicationContext as AppSharedPreferencesImpl).putNightMode(checked)
            (applicationContext as AppSharedPreferencesImpl).switchTheme(checked)
        }

        val backButton = findViewById<ImageButton>(R.id.arrow_back_button)
        backButton.setOnClickListener {
            finish()
        }

        val shareButton = findViewById<ImageButton>(R.id.share_button)
        shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.practicum_link))
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        val supportButton = findViewById<ImageButton>(R.id.support_button)
        supportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            val email = getString(R.string.support_email)
            val emailSubject = getString(R.string.support_email_subject)
            val message = getString(R.string.support_email_message)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            supportIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(supportIntent, emailSubject))
        }

        val agreementButton = findViewById<ImageButton>(R.id.agreement_button)
        val agreementButtonClickListener = View.OnClickListener {
            val web = getString(R.string.agreement_link)
            val agreementIntent = Intent((Intent.ACTION_VIEW), Uri.parse(web))
            startActivity(agreementIntent)
        }
        agreementButton.setOnClickListener(agreementButtonClickListener)
    }
}