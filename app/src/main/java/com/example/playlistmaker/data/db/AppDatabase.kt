package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.favorites.TrackDao
import com.example.playlistmaker.data.db.favorites.TrackEntity
import com.example.playlistmaker.data.db.playlists.PlaylistDao
import com.example.playlistmaker.data.db.playlists.PlaylistEntity
import com.example.playlistmaker.data.db.playlists.PlaylistTrackEntity
import com.example.playlistmaker.data.db.playlists.PlaylistTracksDao

@Database(version = 5, entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTrackDao(): TrackDao

    abstract fun getPlaylistDao(): PlaylistDao

    abstract fun getPlaylistTracks(): PlaylistTracksDao
}