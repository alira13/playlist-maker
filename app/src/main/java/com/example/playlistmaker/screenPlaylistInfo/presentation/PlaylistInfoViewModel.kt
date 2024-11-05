package com.example.playlistmaker.screenPlaylistInfo.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.core.domain.models.Playlist
import com.example.playlistmaker.core.domain.models.Track
import com.example.playlistmaker.screenPlaylistInfo.domain.PlaylistInfoInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale

class PlaylistInfoViewModel(
    private var playlist: Playlist,
    private val playlistInfoInteractor: PlaylistInfoInteractor
) : ViewModel() {

    private var _state = MutableLiveData<PlaylistInfoState>()
    var state: LiveData<PlaylistInfoState> = _state

    private var tracks: List<Track> = emptyList()

    private suspend fun getPlaylist(): Playlist {
        return playlistInfoInteractor.getPlaylist(playlist.playlistId).first()
    }

    suspend fun getTracks(): List<Track> {
        return playlistInfoInteractor.getTracks(playlist.trackIds).first()
    }

    fun getPlaylistInfo() {
        viewModelScope.launch {
            playlist = getPlaylist()
            tracks = getTracks()
            if (tracks.isEmpty()) {
                val info = PlaylistInfo(
                    playlist = playlist,
                    tracks = null
                )
                _state.postValue(
                    PlaylistInfoState.Empty(
                        info
                    )
                )
            } else {
                _state.postValue(
                    PlaylistInfoState.NotEmpty(
                        PlaylistInfo(
                            playlist = playlist,
                            tracks = tracks
                        )
                    )
                )
            }
        }
    }

    fun deleteTrack(track: Track) {
        val newTracksIds = playlist.trackIds.toMutableList()
        newTracksIds.remove((track.trackId))
        val updatedPlaylist = playlist.copy(trackIds = newTracksIds)
        viewModelScope.launch {
            playlistInfoInteractor.updatePlaylist(updatedPlaylist)
            getPlaylistInfo()
            playlistInfoInteractor.deleteTrackFromTable(track)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            playlistInfoInteractor.deletePlaylist(playlist, tracks)
            _state.postValue(PlaylistInfoState.PlaylistDeleted)
        }
    }

    fun sharePlaylist(context: Context) {
        if (tracks.isEmpty()) {
            _state.value = PlaylistInfoState.NothingToShare(feedbackWasShown = false)
            return
        }
        val playlistDescription = buildString {
            appendLine(context.getString(R.string.lbl_playlist, playlist.playlistName))
            if (playlist.playlistDescription.isNotEmpty()) {
                appendLine(playlist.playlistDescription)
            }
            appendLine(
                context.resources.getQuantityString(
                    R.plurals.track_amount,
                    tracks.size,
                    tracks.size
                )
            )
            tracks.forEachIndexed { index, track ->
                appendLine("${index + 1}. ${track.artistName} - ${track.trackName} (${getTime(track.trackTime)})")
            }
        }
        try {
            playlistInfoInteractor.sharePlaylist(playlistDescription)
        } catch (t: Throwable) {
            _state.value = PlaylistInfoState.NoApplicationFound(feedbackWasShown = false)
        }
    }

    private fun getTime(trackTime: Long): String {
        return java.text.SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
    }
}