package com.example.playlistmaker.presentation.mediaScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaFragment : Fragment() {

    private var binding: FragmentMediaBinding? = null

    private var tabMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentMediaBinding.inflate(inflater, container, false)

        binding?.viewPager?.adapter =
            MediaViewPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding!!.tabLayout, binding!!.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.media_favorites_tab)
                1 -> tab.text = getString(R.string.media_playlists_tab)
            }
        }
        tabMediator?.attach()
        return binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator?.detach()
    }
}