package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track

interface SearchRepository {
    fun search(text:String): Resource<List<Track>>
}