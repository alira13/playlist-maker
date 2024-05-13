package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.NetworkResponse

interface TrackNetworkClient {
    suspend fun search(track: String): NetworkResponse
}