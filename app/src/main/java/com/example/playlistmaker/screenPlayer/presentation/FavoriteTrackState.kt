package com.example.playlistmaker.screenPlayer.presentation

import com.example.playlistmaker.R

sealed class FavoriteTrackState(val buttonImage: Int) {

    class Favorite : FavoriteTrackState(R.drawable.liked_button)

    class NotFavorite : FavoriteTrackState(R.drawable.notlike_button)
}