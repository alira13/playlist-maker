package com.example.playlistmaker.data.db.playlists

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToPlaylists(track: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

}