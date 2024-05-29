package com.example.playlistmaker.domain.usecases.playlists

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.PlaylistInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistInfoInteractorImpl(
    private val playlistInfoRepository: PlaylistInfoRepository
) : PlaylistInfoInteractor {

    override fun getPlaylist(playlistId: Int): Flow<Playlist> = flow {
        playlistInfoRepository.getPlaylist(playlistId).collect {
            emit(it)
        }
    }

    override fun getTracks(tracksId: List<Int>): Flow<List<Track>> {
        return playlistInfoRepository.getTracks(tracksId)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistInfoRepository.updatePlaylist(playlist)
    }

    override suspend fun deleteTrackFromTable(track: Track) {
        playlistInfoRepository.deleteTrackFromTable(track)
    }

    override suspend fun deletePlaylist(playlist: Playlist, tracks: List<Track>) {
        playlistInfoRepository.deletePlaylist(playlist, tracks)
    }

    override fun sharePlaylist(description: String) {
        playlistInfoRepository.sharePlaylist(description)
    }
}