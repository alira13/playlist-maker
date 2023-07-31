package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val searchButtonClickListener=object: View.OnClickListener{
            override fun onClick(p0: View?) {
                Toast.makeText(this@MainActivity, "Вы нажали на кнопку \"Поиск\"", Toast.LENGTH_SHORT).show()
            }
        }
        searchButton.setOnClickListener(searchButtonClickListener)

        val mediaButton = findViewById<Button>(R.id.media_button)
        val mediaButtonClickListener=View.OnClickListener { Toast.makeText(this@MainActivity, "Вы нажали на кнопку \"Медиа\"", Toast.LENGTH_SHORT).show() }
        mediaButton.setOnClickListener(mediaButtonClickListener)

        val settingsButton = findViewById<Button>(R.id.settings_button)
        val settingsButtonClickListener= View.OnClickListener { Toast.makeText(this@MainActivity, "Вы нажали на кнопку \"Настройки\"", Toast.LENGTH_SHORT).show() }
        settingsButton.setOnClickListener(settingsButtonClickListener)

    }
}