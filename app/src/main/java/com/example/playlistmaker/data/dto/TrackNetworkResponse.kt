package com.example.playlistmaker.data.dto

import com.example.playlistmaker.core.domain.models.Track

class TrackNetworkResponse(
    val results: List<Track>
) : NetworkResponse()