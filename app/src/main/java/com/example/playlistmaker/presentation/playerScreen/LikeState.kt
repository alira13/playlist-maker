package com.example.playlistmaker.presentation.playerScreen

import com.example.playlistmaker.R

sealed class LikeState(val isButtonEnabled: Boolean, val buttonImage: Int) {

    class Liked : LikeState(true, R.drawable.liked_button)

    class NotLiked : LikeState(false, R.drawable.notlike_button)
}