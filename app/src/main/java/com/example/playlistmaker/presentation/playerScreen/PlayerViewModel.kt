package com.example.playlistmaker.presentation.playerScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerListener
import com.example.playlistmaker.domain.usecases.player.PlayerInteractor

class PlayerViewModel(
    private val track: Track,
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private var screenStateLiveData = MutableLiveData<Track>(track)
    fun getScreenStateLiveData(): LiveData<Track> = screenStateLiveData

    fun preparePlayer(playerListener: PlayerListener) {
        playerInteractor.setListener(playerListener)
        playerInteractor.prepare(screenStateLiveData.value!!.previewUrl)
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

    companion object {
        fun getViewModelFactory(track: Track): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val playerInteractor = Creator.provideTrackInteractor()

                PlayerViewModel(
                    track,
                    playerInteractor,
                )
            }
        }
    }
}