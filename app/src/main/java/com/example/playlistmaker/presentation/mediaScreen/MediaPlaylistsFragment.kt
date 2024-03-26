package com.example.playlistmaker.presentation.mediaScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentMediaFavoritesContentBinding
import com.example.playlistmaker.databinding.FragmentMediaPlaylistsContentBinding

class MediaPlaylistsFragment : Fragment() {
    companion object {
        private const val TEXT = "text"

        fun newInstance(text: String) = MediaPlaylistsFragment().apply {
            arguments = Bundle().apply {
                putString(TEXT, text)
            }
        }
    }

    private lateinit var binding: FragmentMediaPlaylistsContentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaPlaylistsContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emptyPlaylistsErrorTv.text = requireArguments().getString(TEXT).toString()
    }
}