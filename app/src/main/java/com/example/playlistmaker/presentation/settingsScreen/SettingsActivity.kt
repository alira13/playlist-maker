package com.example.playlistmaker.presentation.settingsScreen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.models.ThemeSettings

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        binding.nightModeSwitch.isChecked = settingsViewModel.getThemeSettings().isChecked
        binding.nightModeSwitch.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.updateThemeSetting(ThemeSettings(checked))
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.shareBtn.setOnClickListener {
            settingsViewModel.shareApp()
        }

        binding.supportBtn.setOnClickListener {
            settingsViewModel.openSupport()
        }

        binding.agreementBtn.setOnClickListener {
            settingsViewModel.openTerms()
        }
    }
}