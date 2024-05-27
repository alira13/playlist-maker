package com.example.playlistmaker.domain.usecases.playlists

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInfoInteractor {
    fun getTracksByIds(ids: List<Int>): Flow<List<Track>>
}