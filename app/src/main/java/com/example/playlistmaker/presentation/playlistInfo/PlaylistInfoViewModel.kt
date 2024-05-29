package com.example.playlistmaker.presentation.playlistInfo

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecases.playlists.PlaylistInfoInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlaylistInfoViewModel(
    private var playlist: Playlist,
    private val playlistInfoInteractor: PlaylistInfoInteractor
) : ViewModel() {

    private var _state = MutableLiveData<PlaylistInfoState>()
    var state: LiveData<PlaylistInfoState> = _state

    private var tracks: List<Track> = emptyList()

    private fun getPlaylist(): Playlist {
        return playlist
    }

    suspend fun getTracks(): List<Track> {
        return playlistInfoInteractor.getTracks(playlist.trackIds).first()
    }

    fun getPlaylistInfo() {
        viewModelScope.launch {
            playlist = getPlaylist()
            tracks = getTracks()
            Log.d("MY", "playlist $playlist")
            Log.d("MY", "tracks $tracks")
            if (tracks.isEmpty()) {
                Log.d("MY", "Empty")
                val info = PlaylistInfo(
                    playlist = playlist,
                    tracks = null
                )
                Log.d("MY", "info ${info.toString()}")
                _state.postValue(
                    PlaylistInfoState.Empty(
                        info
                    )
                )
                Log.d("MY", "Empty $playlist")
            } else {
                Log.d("MY", "NotEmpty $playlist")
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
        newTracksIds.remove(track.trackId)
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
    }
}