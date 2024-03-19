package com.example.playlistmaker.presentation.settingsScreen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.models.ThemeSettings
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: ActivitySettingsBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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