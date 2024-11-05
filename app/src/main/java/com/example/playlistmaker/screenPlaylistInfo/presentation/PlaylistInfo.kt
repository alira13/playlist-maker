package com.example.playlistmaker.screenPlaylistInfo.presentation

import android.icu.text.SimpleDateFormat
import com.example.playlistmaker.core.domain.models.Playlist
import com.example.playlistmaker.core.domain.models.Track
import java.util.Locale

class PlaylistInfo(val playlist: Playlist, val tracks: List<Track>?) {
    val totalDuration: Int
        get() {
            return getTotalDuration(tracks)
        }
}

private fun getTotalDuration(tracks: List<Track>?): Int {
    var sum = 0L
    if (!tracks.isNullOrEmpty()) {
        tracks.forEach {
            sum += it.trackTime
        }
        return SimpleDateFormat("m", Locale.getDefault()).format(sum).toInt()
    }
    return 0
}
