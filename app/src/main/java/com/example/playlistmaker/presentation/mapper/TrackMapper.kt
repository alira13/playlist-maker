package com.example.playlistmaker.presentation.mapper

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.models.PlayerTrackInfo
import java.text.SimpleDateFormat
import java.util.Locale

object TrackMapper {
    fun map(track: Track):PlayerTrackInfo {
        return PlayerTrackInfo(
        trackName = track.trackName,
        artistName = track.artistName,
        trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime),
        collectionName = track.collectionName,
        releaseDate = if (track.releaseDate.isNotEmpty()) track.releaseDate.substring(
            0, 4
        ) else "Not found",
        primaryGenreName = track.primaryGenreName,
        country = track.country,
        artworkUrl512 = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
    }
}