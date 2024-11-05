package com.example.playlistmaker.data.db.playlists

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistTracksDao {
    @Insert(entity = PlaylistTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_track_table")
    suspend fun getTracks(): List<PlaylistTrackEntity>

    @Delete
    suspend fun deleteTrack(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_track_table where trackId = :trackId")
    suspend fun getTrackById(trackId: Int): PlaylistTrackEntity
}