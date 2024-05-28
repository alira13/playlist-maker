package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.favorites.TrackEntity
import com.example.playlistmaker.data.db.playlists.PlaylistEntity

@Dao
interface PlaylistTracksDao {
    @Insert(entity = PlaylistTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_track_table")
    suspend fun getTracks(): List<TrackEntity>
}