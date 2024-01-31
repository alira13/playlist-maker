package com.example.playlistmaker.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R

class MediaActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val button = findViewById<ImageButton>(R.id.arrow_back_button)
        val buttonClickListener = View.OnClickListener {
            finish()
        }
        button.setOnClickListener(buttonClickListener)
    }
}