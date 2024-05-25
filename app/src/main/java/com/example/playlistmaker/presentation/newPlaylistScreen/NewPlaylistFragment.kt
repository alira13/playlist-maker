package com.example.playlistmaker.presentation.newPlaylistScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.presentation.rootScreen.RootActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class NewPlaylistFragment : Fragment() {
    private val viewModel by viewModel<NewPlaylistViewModel>()
    private var binding: FragmentNewPlaylistBinding? = null

    private var onCloseDialog: MaterialAlertDialogBuilder? = null
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

        (activity as? RootActivity)?.hideBottomNavigation()

        createOnCloseDialog()
        createPickMedia()
        addTextWatcher()

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

    private fun createOnCloseDialog() {
        onCloseDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.complete_playlist_creation))
            setMessage(getString(R.string.unsaved_data_losing))
            setPositiveButton(getString(R.string.complete)) { _, _ ->
                findNavController().navigateUp()
            }
            setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }
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
            onCloseDialog?.show()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun onImageClick() {
        pickMedia?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun onCreateClick() {
        val playlistName = binding?.playlistNameTiEt?.text.toString()

        var playlistDescription = binding?.descriptionTiEt?.text.toString()
        if (playlistDescription.isEmpty()) playlistDescription = ""

        val playlistImageName: String?
        if (playlistImageUri != null) {
            playlistImageName = playlistName + Calendar.getInstance().time.toString() + ".jpg"
            savePlaylistImage(playlistImageName)
        } else playlistImageName = ""

        viewModel.createNewPlayList(playlistName, playlistDescription, playlistImageName)

        val toast = Toast(context)
        toast.setGravity(Gravity.BOTTOM, 0, 0)
        toast.duration = Toast.LENGTH_LONG
        toast.setText("Плейлист $playlistName создан")
        toast.show()

        findNavController().navigateUp()
    }

    private fun savePlaylistImage(playlistImageName: String) {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, playlistImageName)
        val inputStream = requireActivity().contentResolver.openInputStream(playlistImageUri!!)
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, QUALITY, outputStream)

        inputStream!!.close()
        outputStream.close()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as? RootActivity)?.hideBottomNavigation()
    }
    companion object {
        private const val DIRECTORY = "playlists_images"
        private const val QUALITY = 100
    }
}