package com.example.playlistmaker.presentation.playerScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerListener
import com.example.playlistmaker.domain.usecases.player.PlayerInteractor
import com.example.playlistmaker.domain.usecases.playlists.PlaylistsInteractor
import com.example.playlistmaker.presentation.mediaScreen.playlists.PlaylistTrackState
import com.example.playlistmaker.presentation.mediaScreen.playlists.PlaylistsState
import com.example.playlistmaker.presentation.models.PlaylistInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: Track,
    private val playerInteractor: PlayerInteractor,
    private val playlisInteractor: PlaylistsInteractor
) : ViewModel() {

    private var _screenStateLiveData = MutableLiveData<Track>(track)
    var screenStateLiveData: LiveData<Track> = _screenStateLiveData

    private var _playerState = MutableLiveData<PlayerState>()
    var playerState: LiveData<PlayerState> = _playerState

    private var _isFavorite = MutableLiveData<FavoriteTrackState>()
    var isLiked: LiveData<FavoriteTrackState> = _isFavorite

    private var _playlistsState = MutableLiveData<PlaylistsState>()
    var playlistsState: LiveData<PlaylistsState> = _playlistsState

    private var _playlistTrackState = MutableLiveData<PlaylistTrackState>()
    var playlistTrackState: LiveData<PlaylistTrackState> = _playlistTrackState

    private var timerJob: Job? = null

    fun preparePlayer() {
        playerInteractor.setListener(getPlayerListener())
        playerInteractor.prepare(_screenStateLiveData.value!!.previewUrl)

        _playerState.postValue(PlayerState.Prepared())
        getLikeState()
    }


    fun getLikeState() {
        if (track.isFavorite) {
            _isFavorite.value = FavoriteTrackState.Favorite()
        } else {
            _isFavorite.value = FavoriteTrackState.NotFavorite()
        }
    }

    private fun getPlayerListener() = object : PlayerListener {
        override fun onPrepare() {
            _playerState.postValue(PlayerState.Prepared())
        }

        override fun onCompletion() {
            _playerState.postValue(PlayerState.Prepared())
        }
    }

    fun onPlayButtonClicked() {
        when (_playerState.value) {
            is PlayerState.Playing -> {
                pause()
            }

            is PlayerState.Paused, is PlayerState.Prepared -> {
                play()
            }

            else -> {
                Unit
            }
        }
    }

    private fun play() {
        playerInteractor.play()
        _playerState.postValue(PlayerState.Paused(getCurrentTime()))
        startTimer()
    }

    fun pause() {
        playerInteractor.pause()
        timerJob?.cancel()
        _playerState.postValue(PlayerState.Paused(getCurrentTime()))
    }

    private fun releasePlayer() {
        playerInteractor.quit()
        _playerState.postValue(PlayerState.Default())
    }


    fun getPlaylists() {
        viewModelScope.launch {
            playlisInteractor.getPlaylists().collect {
                if (it.isEmpty()) {
                    _playlistsState.postValue(PlaylistsState.EmptyPlaylists())
                } else {
                    _playlistsState.postValue(PlaylistsState.ShowPlaylists(it))
                }
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(UPDATE_TIMER_DELAY_MILLIS)
                val time = getCurrentTime()
                _playerState.postValue(PlayerState.Playing(time))
            }
            _playerState.postValue(PlayerState.Prepared())
        }
    }

    private fun getCurrentTime(): String {
        return playerInteractor.getCurrentTime()
    }

    fun onLikeButtonClicked() {
        viewModelScope.launch {


            when (_isFavorite.value) {
                is FavoriteTrackState.Favorite -> {

                    playerInteractor.deleteFromFavorites(track)
                    track.isFavorite = false
                    _isFavorite.postValue(FavoriteTrackState.NotFavorite())
                }

                is FavoriteTrackState.NotFavorite -> {

                    track.isFavorite = true
                    playerInteractor.addToFavorites(track)
                    _isFavorite.value = (FavoriteTrackState.Favorite())

                }

                else -> {

                    Unit
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun onPlaylistItemClicked(playlist: PlaylistInfo) {
        viewModelScope.launch {
            val track = _screenStateLiveData.value!!
            if (playlist.trackIds.contains(track.trackId)) {
                _playlistTrackState.postValue(
                    PlaylistTrackState.Exist(
                        playlist,
                        track
                    )
                )

            } else {
                val trackIds = playlist.trackIds.toMutableList()
                trackIds.add(track.trackId)
                playlist.trackIds = trackIds.toList()
                playlisInteractor.addToPlaylist(playlist)
                _playlistTrackState.postValue(
                    PlaylistTrackState.NotExist(
                        playlist,
                        track
                    )
                )
            }
        }
    }

    companion object {
        private const val UPDATE_TIMER_DELAY_MILLIS = 1000L
    }
}