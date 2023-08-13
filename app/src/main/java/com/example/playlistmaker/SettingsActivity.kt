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

        /*val shareButton = findViewById<ImageButton>(R.id.share_button)
        val shareButtonClickListener = View.OnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            val message = "https://practicum.yandex.ru/android-developer/?from=catalog"
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }
        shareButton.setOnClickListener(shareButtonClickListener)

        val supportButton = findViewById<ImageButton>(R.id.support_button)
        val supportButtonClickListener = View.OnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            val email="Bezglasnaya@gmail.com"
            val emailSubject="Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val message = " «Спасибо разработчикам и разработчицам за крутое приложение!»"
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            supportIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(supportIntent)
        }
        supportButton.setOnClickListener(supportButtonClickListener)
 */
        val agreementButton = findViewById<ImageButton>(R.id.agreement_button)
        val agreementButtonClickListener = View.OnClickListener {
            val web = "https://yandex.ru/legal/practicum_offer/"
            val agreementIntent=Intent((Intent.ACTION_VIEW), Uri.parse(web))
            startActivity(agreementIntent)
        }
        agreementButton.setOnClickListener(agreementButtonClickListener)
    }
}