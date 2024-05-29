package com.example.playlistmaker.data.db.favorites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    var trackId: Int,

    val trackName: String,

    val artistName: String,

    val trackTime: Long,

    val artworkUrl100: String,

    val collectionName: String,

    val releaseDate: String,

    val primaryGenreName: String,

    val country: String,

    val previewUrl: String
)