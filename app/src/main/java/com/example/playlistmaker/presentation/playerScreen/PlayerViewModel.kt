package com.example.playlistmaker.presentation.playerScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerListener
import com.example.playlistmaker.domain.usecases.player.PlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: Track, private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private var _screenStateLiveData = MutableLiveData<Track>(track)
    var screenStateLiveData: LiveData<Track> = _screenStateLiveData

    private var _playerState = MutableLiveData<PlayerState>()
    var playerState: LiveData<PlayerState> = _playerState

    private var _isFavorite = MutableLiveData<FavoriteTrackState>()
    var isLiked: LiveData<FavoriteTrackState> = _isFavorite

    private var timerJob: Job? = null

    fun preparePlayer() {
        playerInteractor.setListener(getPlayerListener())
        playerInteractor.prepare(_screenStateLiveData.value!!.previewUrl)
        Log.d("MY_LOG", "preparePlayer _screenStateLiveData = ${_screenStateLiveData.value}")
        _playerState.postValue(PlayerState.Prepared())
        getLikeState()
    }

    private fun getLikeState() {
        if (track.isFavorite) {
            Log.d(
                "MY_LOG",
                "getLikeState isButtonEnabled = ${FavoriteTrackState.Favorite().isButtonEnabled}"
            )
            _isFavorite.value = FavoriteTrackState.Favorite()
        } else {
            Log.d(
                "MY_LOG",
                "getLikeState isButtonEnabled = ${FavoriteTrackState.NotFavorite().isButtonEnabled}"
            )
            _isFavorite.value = FavoriteTrackState.NotFavorite()
            Log.d("MY_LOG", "postValue _isFavorite = ${_isFavorite.value}")
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
            Log.d("MY_LOG", "onLikeButtonClicked _isFavorite=${_isFavorite.value})")

            when (_isFavorite.value) {
                is FavoriteTrackState.Favorite -> {
                    Log.d("MY_LOG", "onLikeButtonClicked _isLiked=true")
                    playerInteractor.deleteFromFavorites(track)
                    _isFavorite.postValue(FavoriteTrackState.NotFavorite())
                }

                is FavoriteTrackState.NotFavorite -> {
                    Log.d("MY_LOG", "onLikeButtonClicked _isLiked=false")
                    playerInteractor.addToFavorites(track)
                    _isFavorite.postValue(FavoriteTrackState.Favorite())
                }

                else -> {
                    Log.d("MY_LOG", "onLikeButtonClicked else")
                    Unit
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    companion object {
        private const val UPDATE_TIMER_DELAY_MILLIS = 1000L
    }
}