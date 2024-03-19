package com.example.playlistmaker.presentation.playerScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerListener
import com.example.playlistmaker.domain.usecases.player.PlayerInteractor

class PlayerViewModel(
    private val track: Track,
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private var _screenStateLiveData = MutableLiveData<Track>(track)
    var screenStateLiveData: LiveData<Track> = _screenStateLiveData

    fun preparePlayer(playerListener: PlayerListener) {
        playerInteractor.setListener(playerListener)
        playerInteractor.prepare(_screenStateLiveData.value!!.previewUrl)
    }

    fun play() {
        playerInteractor.play()
    }

    fun pause() {
        playerInteractor.pause()
    }

    fun getCurrentTime(): String {
        return playerInteractor.getCurrentTime()
    }

    fun quit() {
        playerInteractor.quit()
    }


}