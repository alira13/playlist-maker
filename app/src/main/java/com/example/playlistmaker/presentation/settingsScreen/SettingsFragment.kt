package com.example.playlistmaker.presentation.settingsScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.domain.models.ThemeSettings
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val settingsViewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("MY_LOG", "SettingsFragment onCreateView start")
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        Log.d("MY_LOG", "SettingsFragment onCreateView finish")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nightModeSwitch.isChecked = settingsViewModel.getThemeSettings().isChecked
        binding.nightModeSwitch.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.updateThemeSetting(ThemeSettings(checked))
        }

        binding.backBtn.setOnClickListener {
            activity?.finish()
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