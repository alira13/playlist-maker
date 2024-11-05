package com.example.playlistmaker.presentation.screenMain

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private var binding: ActivityRootBinding? = null

    private val viewModel by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initTheme()

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.content_fcv) as NavHostFragment
        val navController = navHostFragment.navController

        binding?.navigationBnv?.setupWithNavController(navController)
    }

    fun showBottomNavigation() {
        binding!!.navigationBnv.visibility = View.VISIBLE
    }

    fun hideBottomNavigation() {
        binding!!.navigationBnv.visibility = View.GONE
    }
}