package com.example.playlistmaker.presentation.playerScreen

import com.example.playlistmaker.R

sealed class FavoriteState(val isButtonEnabled: Boolean, val buttonImage: Int) {

    class Favorite() : FavoriteState(true, R.drawable.liked_button)

    class NotFavorite() : FavoriteState(false, R.drawable.notlike_button)
}