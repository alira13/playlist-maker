package com.example.playlistmaker.core.presentation

import com.example.playlistmaker.core.domain.models.Track

interface TrackItemClickListener {
    fun onClick(track: Track)
}