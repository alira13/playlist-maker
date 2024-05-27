package com.example.playlistmaker.domain.usecases.playlists

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.PlaylistInfoRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInfoInteractorImpl(
    private val playlistInfoRepository: PlaylistInfoRepository
) : PlaylistInfoInteractor {

    override fun getTracksByIds(ids:List<Int>): Flow<List<Track>> {
        return playlistInfoRepository.getTracksByIds(ids)
    }
}