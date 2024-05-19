package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.playlists.PlaylistEntity
import com.example.playlistmaker.presentation.models.PlaylistInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConverter(private val json: Gson) {
    fun map(track: PlaylistInfo): PlaylistEntity {
        return PlaylistEntity(
            playlistId = track.playlistId,
            playlistName = track.playlistName,
            playlistDescription = track.playlistDescription,
            tracksNum = track.tracksNum,
            artworkUrl512 = track.artworkUrl512,
            trackIds = json.toJson(track.trackIds)
        )
    }

    fun map(track: PlaylistEntity): PlaylistInfo {
        val token = object : TypeToken<List<Int>>() {}.type
        val trackIds = json.fromJson<List<Int>>(track.trackIds, token)
        val playlistInfo = PlaylistInfo(
            playlistId = track.playlistId,
            playlistName = track.playlistName,
            playlistDescription = track.playlistDescription,
            tracksNum = track.tracksNum,
            artworkUrl512 = track.artworkUrl512,
            trackIds = trackIds
        )
        return playlistInfo
    }
}