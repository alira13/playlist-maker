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
    private val track: Track,
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private var _screenStateLiveData = MutableLiveData<Track>(track)
    var screenStateLiveData: LiveData<Track> = _screenStateLiveData

    private var _playerState = MutableLiveData<PlayerState>()
    var playerState: LiveData<PlayerState> = _playerState

    private var timerJob: Job? = null

    fun preparePlayer() {
        playerInteractor.setListener(getPlayerListener())
        playerInteractor.prepare(_screenStateLiveData.value!!.previewUrl)
        _playerState.postValue(PlayerState.Prepared())
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
        Log.d("MY_LOG", "onPlayButtonClicked ${_playerState.value}")
        when (_playerState.value) {
            is PlayerState.Playing -> {
                Log.d("MY_LOG", "pausePlayer")
                pause()
            }

            is PlayerState.Paused, is PlayerState.Prepared -> {
                Log.d("MY_LOG", "startPlayer")
                play()
            }

            else -> {
                Log.d("MY_LOG", "no identified")
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
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(UPDATE_TIMER_DELAY_MILLIS)
                val time = getCurrentTime()
                Log.d("MY_LOG", "getCurrentTime=$time")
                _playerState.postValue(PlayerState.Playing(time))
            }
            _playerState.postValue(PlayerState.Prepared())
        }
    }

    private fun getCurrentTime(): String {
        return playerInteractor.getCurrentTime()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    companion object {
        private const val UPDATE_TIMER_DELAY_MILLIS = 1000L
    }
}