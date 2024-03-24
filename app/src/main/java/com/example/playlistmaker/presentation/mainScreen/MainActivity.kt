package com.example.playlistmaker.presentation.mainScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.mediaScreen.MediaActivity
import com.example.playlistmaker.presentation.searchScreen.SearchActivity
import com.example.playlistmaker.presentation.settingsScreen.MainViewModel
import com.example.playlistmaker.presentation.settingsScreen.SettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.initTheme()
        setButtonOnClickListener(R.id.search_request_iv, SearchActivity::class.java)
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