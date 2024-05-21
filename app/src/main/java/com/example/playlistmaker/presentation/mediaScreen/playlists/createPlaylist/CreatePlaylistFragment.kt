package com.example.playlistmaker.presentation.mediaScreen.playlists.createPlaylist

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CreatePlaylistFragment : Fragment() {
    var binding: FragmentCreatePlaylistBinding? = null

    private var dialog: MaterialAlertDialogBuilder? = null
    private var albumImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.complete_playlist_creation))
            setMessage(getString(R.string.unsaved_data_losing))
            setPositiveButton(getString(R.string.complete)) { _, _ ->
                findNavController().navigateUp()
            }
            setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }
        }

        binding?.backButton?.setOnClickListener {
            if (albumImageUri != null ||
                !binding?.playlistNameTiEd?.text.isNullOrEmpty() ||
                !binding?.descriptionTiEt?.text.isNullOrEmpty()
            ) {
                dialog?.show()
            } else {
                findNavController().navigateUp()
            }
        }

        addPlaylistNameTextWatcher()
    }

    protected fun addPlaylistNameTextWatcher() {
        binding?.playlistNameTiEd?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding?.createButton?.isEnabled = !s.isNullOrBlank()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}