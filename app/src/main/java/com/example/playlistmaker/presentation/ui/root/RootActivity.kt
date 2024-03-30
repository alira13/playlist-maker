package com.example.playlistmaker.presentation.ui.root

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.example.playlistmaker.presentation.mainScreen.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    private val viewModel by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MY_LOG", "RootActivity: onCreate")

        viewModel.initTheme()

        // Привязываем вёрстку к экрану
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*if (savedInstanceState == null) {
            Log.d("MY_LOG", "savedInstanceState=null")
            // Добавляем фрагмент в контейнер
            supportFragmentManager.commit {
                //this.add(R.id.rootFragmentContainerView, SearchFragment())
                this.add(R.id.container_view, SettingsFragment())
            }
        }
         */

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
    }
}