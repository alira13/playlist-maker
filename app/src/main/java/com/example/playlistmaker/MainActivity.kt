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

        setButtonOnClickListener(R.id.search_button, SearchActivity::class.java)
        setButtonOnClickListener(R.id.media_button, MediaActivity::class.java)
        setButtonOnClickListener(R.id.settings_button, SettingsActivity::class.java)
    }

    private fun <T : AppCompatActivity> setButtonOnClickListener(
        buttonId: Int,
        activity: Class<T>
    ) {
        val button = findViewById<Button>(buttonId)
        val buttonClickListener = View.OnClickListener {
            val displayIntent = Intent(this, activity)
            startActivity(displayIntent)
        }
        button.setOnClickListener(buttonClickListener)
    }
}