package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.playlists.PlaylistEntity
import com.example.playlistmaker.core.domain.models.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConverter(private val json: Gson) {
    fun map(track: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = track.playlistId,
            playlistName = track.playlistName,
            playlistDescription = track.playlistDescription,
            artworkUrl512 = track.artworkUrl512,
            trackIds = json.toJson(track.trackIds)
        )
    }

    fun map(track: PlaylistEntity): Playlist {
        val token = object : TypeToken<List<Int>>() {}.type
        val trackIds = json.fromJson<List<Int>>(track.trackIds, token)
        val tracksNum = trackIds.size
        val playlistInfo = Playlist(
            playlistId = track.playlistId,
            playlistName = track.playlistName,
            playlistDescription = track.playlistDescription,
            tracksNum = tracksNum,
            artworkUrl512 = track.artworkUrl512,
            trackIds = trackIds
        )
        return playlistInfo
    }
}