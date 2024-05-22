package com.example.playlistmaker.data.db.playlists

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    var playlistId: Int,

    val playlistName: String,

    val playlistDescription: String,

    val artworkUrl512: String,

    var trackIds: String
)