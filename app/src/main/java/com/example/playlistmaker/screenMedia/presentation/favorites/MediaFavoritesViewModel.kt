package com.example.playlistmaker.screenMedia.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.screenPlayer.domain.PlayerInteractor
import kotlinx.coroutines.launch

class MediaFavoritesViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private var _screenStateLiveData = MutableLiveData<FavoritesState>()
    var screenStateLiveData: LiveData<FavoritesState> = _screenStateLiveData

    fun getState() {
        viewModelScope.launch {
            playerInteractor.favoritesTracks().collect {
                if (it.isEmpty()) {
                    _screenStateLiveData.postValue(FavoritesState.IsEmpty())
                } else {
                    _screenStateLiveData.postValue(FavoritesState.NotEmpty(it))
                }
            }
        }
    }
}