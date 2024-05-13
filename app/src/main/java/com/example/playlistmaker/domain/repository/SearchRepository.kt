package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun search(text: String): Flow<Resource<List<Track>>>
}