package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.SearchRepository
import java.util.concurrent.Executors

class SearchInteractorImpl(private val currencyRepository: SearchRepository):SearchInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun execute(text: String, consumer: Consumer<Track>) {
        executor.execute {
            when (val currencyResponse = currencyRepository.search(text)) {
                is Resource.Success -> {
                    val productDetails = currencyResponse.data
                    consumer.consume(ConsumerData.Data(productDetails))
                }
                is Resource.NetworkError -> {
                    consumer.consume(ConsumerData.NetworkError(""))
                }
                is Resource.EmptyListError -> {
                    consumer.consume(ConsumerData.EmptyListError(""))
                }
            }
        }
    }
}