package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setButtonOnClickListener(R.id.search_button, SearchActivity())
        setButtonOnClickListener(R.id.media_button, MediaActivity())
        setButtonOnClickListener(R.id.settings_button, SettingsActivity())
    }

    private fun <T : AppCompatActivity> setButtonOnClickListener(buttonId: Int, activity: T) {
        val button = findViewById<Button>(buttonId)
        val buttonClickListener = View.OnClickListener {
            val displayIntent = Intent(this, activity::class.java)
            startActivity(displayIntent)
        }
        button.setOnClickListener(buttonClickListener)
    }
}