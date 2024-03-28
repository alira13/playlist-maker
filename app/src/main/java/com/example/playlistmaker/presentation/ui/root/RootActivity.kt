package com.example.playlistmaker.presentation.ui.root

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.example.playlistmaker.presentation.searchScreen.SearchFragment
import com.example.playlistmaker.presentation.settingsScreen.SettingsFragment

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MY_LOG", "RootActivity: onCreate")
        // Привязываем вёрстку к экрану
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            Log.d("MY_LOG", "savedInstanceState=null")
            // Добавляем фрагмент в контейнер
            supportFragmentManager.commit {
                //this.add(R.id.rootFragmentContainerView, SearchFragment())
                this.add(R.id.rootFragmentContainerView, SettingsFragment())
            }
        }
    }
}