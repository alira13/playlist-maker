package com.example.playlistmaker.screenSearch.domain.repository

import com.example.playlistmaker.screenSearch.domain.models.Resource
import com.example.playlistmaker.core.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun search(text: String): Flow<Resource<List<Track>>>
}