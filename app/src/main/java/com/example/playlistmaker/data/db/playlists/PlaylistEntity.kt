package com.example.playlistmaker.data.db.playlists

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,

    val playlistName: String,

    val playlistDescription: String,

    val artworkUrl512: String,

    val trackIds: String
)