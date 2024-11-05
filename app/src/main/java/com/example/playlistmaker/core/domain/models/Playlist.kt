package com.example.playlistmaker.core.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Playlist(
    var playlistId: Int,
    var playlistName: String,
    val playlistDescription: String,
    val tracksNum: Int,
    val artworkUrl512: String,
    var trackIds: List<Int>
): Parcelable