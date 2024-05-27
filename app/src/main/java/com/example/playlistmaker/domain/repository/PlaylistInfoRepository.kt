package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInfoRepository {
    fun getTracksByIds(ids: List<Int>): Flow<List<Track>>
}