package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.domain.models.Track

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addTrack(track: Track)

    @Delete(entity = TrackEntity::class)
    fun deleteTrack(track: Track)

    @Query("SELECT * FROM track_table")
    fun getTracks(): List<Track>

    @Query("SELECT trackId FROM track_table")
    fun getTrackIds(): List<Int>
}