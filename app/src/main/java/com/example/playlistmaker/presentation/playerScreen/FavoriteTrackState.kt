package com.example.playlistmaker.presentation.playerScreen

import com.example.playlistmaker.R

sealed class FavoriteTrackState(val isButtonEnabled: Boolean, val buttonImage: Int) {

    class Favorite() : FavoriteTrackState(true, R.drawable.liked_button)

    class NotFavorite() : FavoriteTrackState(false, R.drawable.notlike_button)
}