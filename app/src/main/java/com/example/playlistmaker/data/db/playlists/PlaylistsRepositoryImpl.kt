package com.example.playlistmaker.data.db.playlists

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.data.converters.PlaylistTrackDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.core.domain.models.Playlist
import com.example.playlistmaker.core.domain.models.Track
import com.example.playlistmaker.screenSearch.domain.repository.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConverter,
    private val trackDbConvertor: PlaylistTrackDbConvertor,
    private val application: Application
) : PlaylistsRepository {
    override suspend fun addToPlaylist(playlist: Playlist, track: Track) {
        val playlistEntity = playlistDbConvertor.map(playlist)
        val trackEntity = trackDbConvertor.map(track)
        appDatabase.getPlaylistTracks().addTrack(trackEntity)
        appDatabase.getPlaylistDao().addToPlaylists(playlistEntity)
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        val playlistEntity = playlistDbConvertor.map(playlist)
        appDatabase.getPlaylistDao().addToPlaylists(playlistEntity)
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val trackEntity = appDatabase.getPlaylistDao().getPlaylists().reversed()
        val tracks = convertFromPlaylistEntity(trackEntity)
        emit(tracks)
    }

    private fun convertFromPlaylistEntity(tracks: List<PlaylistEntity>): List<Playlist> {
        return tracks.map { track -> playlistDbConvertor.map(track) }
    }

    override fun saveCoverToStorage(uri: Uri?): Uri {
        if (uri == null) {
            return Uri.EMPTY
        }

        val filePath = File(
            application.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            DIRECTORY
        )

        if (!filePath.exists()) {
            filePath.mkdirs()
        }


        if (!uri.scheme.equals("content")) {
            return uri
        }

        val file = File(
            filePath, String.format(
                FILE_NAME,
                System.currentTimeMillis()
            )
        )

        val inputStream = application.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, QUALITY, outputStream)

        inputStream!!.close()
        outputStream.close()
        return Uri.fromFile(file)
    }

    companion object {
        private const val FILE_NAME = "playlist_image%s.jpg"
        private const val DIRECTORY = "playlists_images"
        private const val QUALITY = 100
    }
}