package com.example.playlistmaker.presentation.mediaScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentMediaFavoritesContentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFavoritesFragment : Fragment() {
    companion object {
        private const val TEXT = "text"

        fun newInstance(text: String) = MediaFavoritesFragment().apply {
            arguments = Bundle().apply {
                putString(TEXT, text)
            }
        }
    }

    private lateinit var binding: FragmentMediaFavoritesContentBinding

    private val playerViewModel by viewModel<MediaFavoritesViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaFavoritesContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emptyMediaErrorTv.text = requireArguments().getString(TEXT).toString()
    }
}