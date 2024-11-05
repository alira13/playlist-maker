package com.example.playlistmaker.presentation.screenNewPlaylist

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.core.presentation.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {
    private val viewModel by viewModel<NewPlaylistViewModel>()
    private var binding: FragmentNewPlaylistBinding? = null

    private var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null

    private var playlistImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.hideBottomNavigation()

        createPickMedia()
        addTextWatcher()
        addOnBackPressedCallback()

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        binding?.backButton?.setOnClickListener {
            onBackClick()
        }


        binding?.playerTrackImage?.setOnClickListener {
            onImageClick()
        }

        binding?.createButton?.setOnClickListener {
            onCreateClick()
        }
    }

    private fun addOnBackPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            onBackClick()
        }
    }

    private fun createPickMedia() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Glide.with(binding!!.playerTrackImage).load(uri).placeholder(R.drawable.placeholder)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(
                            binding!!.playerTrackImage.resources.getDimensionPixelSize(
                                R.dimen.player_track_image_corner_radius
                            )
                        )
                    ).into(binding!!.playerTrackImage)
                playlistImageUri = uri
            }
        }
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        ).apply {
            setTitle(getString(R.string.msg_complete_playlist_creation))
            setMessage(getString(R.string.msg_unsaved_data_losing))
            setPositiveButton(getString(R.string.btn_complete)) { _, _ ->
                findNavController().navigateUp()
            }
            setNegativeButton(getString(R.string.btn_cancel)) { _, _ ->
            }.show()
        }
    }

    private fun addTextWatcher() {
        binding?.playlistNameTiEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding?.createButton?.isEnabled = !s.isNullOrBlank()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun onBackClick() {
        if (playlistImageUri != null || !binding?.playlistNameTiEt?.text.isNullOrEmpty() || !binding?.descriptionTiEt?.text.isNullOrEmpty()) {
            showDialog()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun onImageClick() {
        pickMedia?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun onCreateClick() {
        val playlistName = binding?.playlistNameTiEt?.text.toString()
        val playlistDescription = binding?.descriptionTiEt?.text.toString()

        viewModel.createNewPlayList(playlistName, playlistDescription, playlistImageUri)

        showSnackbar(
            requireView(),
            getString(R.string.msg_playlist_created, playlistName)
        )

        findNavController().navigateUp()
    }

    private fun showSnackbar(root: View, text: String) {
        val snackbar =
            Snackbar.make(root, text, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(
            ContextCompat.getColor(
                root.context,
                R.color.YP_grey
            )
        )
        snackbar.setTextColor(
            ContextCompat.getColor(
                root.context,
                R.color.YP_white
            )
        )
        val textView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.hideBottomNavigation()
    }
}