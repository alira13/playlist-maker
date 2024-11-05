package com.example.playlistmaker.screenSettings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.screenSettings.domain.models.ThemeSettings
import com.example.playlistmaker.core.presentation.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val settingsViewModel by viewModel<SettingsViewModel>()
    private var binding: FragmentSettingsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.showBottomNavigation()

        binding?.nightModeSwitch?.isChecked = settingsViewModel.getThemeSettings().isChecked
        binding?.nightModeSwitch?.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.updateThemeSetting(ThemeSettings(checked))
        }

        binding?.shareBtn?.setOnClickListener {
            settingsViewModel.shareApp()
        }

        binding?.supportBtn?.setOnClickListener {
            settingsViewModel.openSupport()
        }

        binding?.agreementBtn?.setOnClickListener {
            settingsViewModel.openTerms()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showBottomNavigation()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}