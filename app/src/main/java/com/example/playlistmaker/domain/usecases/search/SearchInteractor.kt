package com.example.playlistmaker.domain.usecases.search

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.models.Track

interface SearchInteractor {
    fun execute(text: String, consumer: Consumer<Track>)
}