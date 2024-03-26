package com.example.playlistmaker.presentation.mediaScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentMediaPlaylistsContentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlaylistsFragment : Fragment() {

    private val playerViewModel by viewModel<MediaPlaylistsViewModel>()

    private var binding: FragmentMediaPlaylistsContentBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaPlaylistsContentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}