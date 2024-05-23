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
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.presentation.models.NewPlaylist
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class NewPlaylistFragment : Fragment() {
    var binding: FragmentNewPlaylistBinding? = null

    private val viewModel by viewModel<NewPlaylistViewModel>()

    private var dialog: MaterialAlertDialogBuilder? = null
    private var playlistImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)

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
            if (playlistImageUri != null ||
                !binding?.playlistNameTiEd?.text.isNullOrEmpty() ||
                !binding?.descriptionTiEt?.text.isNullOrEmpty()
            ) {
                dialog?.show()
            } else {
                findNavController().navigateUp()
            }
        }

        addPlaylistNameTextWatcher()

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding?.playerTrackImage?.setImageURI(uri)
                    playlistImageUri = uri
                }
            }

        binding?.playerTrackImage?.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding?.createButton?.setOnClickListener {
            val playlistName = binding?.playlistNameTiEd?.text.toString()
            val playlistDescription = binding?.descriptionTiEt?.text.toString()

            var playlistImageName: String? = null
            if (playlistImageUri != null) {
                playlistImageName = playlistName + Calendar.getInstance().time.toString() + ".jpg"
                savePlaylistImage(playlistImageName)
            }

            val playlist = NewPlaylist(playlistName, playlistDescription, playlistImageName)
            viewModel.createPlaylist(playlist)

            val toast = Toast(context)
            toast.setGravity(Gravity.BOTTOM, 0, 0)
            toast.duration = Toast.LENGTH_LONG
            toast.setText("Плейлист $playlistName создан")
            toast.show()

            findNavController().navigateUp()
        }
    }

    private fun savePlaylistImage(playlistImageName: String) {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            DIRECTORY
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, playlistImageName)
        val inputStream =
            requireActivity().contentResolver.openInputStream(playlistImageUri!!)        // создаём входящий поток байтов из выбранной картинки
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, QUALITY, outputStream)

        inputStream!!.close()
        outputStream.close()
    }

    companion object {
        private const val DIRECTORY = "playlists_images"
        private const val QUALITY = 100
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