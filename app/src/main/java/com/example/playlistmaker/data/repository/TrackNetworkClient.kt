package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.NetworkResponse

interface TrackNetworkClient {
    fun search(currency: String): NetworkResponse
}