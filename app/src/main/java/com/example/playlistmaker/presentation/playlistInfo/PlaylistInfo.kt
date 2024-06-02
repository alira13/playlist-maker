package com.example.playlistmaker.presentation.playlistInfo

import android.icu.text.SimpleDateFormat
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
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
