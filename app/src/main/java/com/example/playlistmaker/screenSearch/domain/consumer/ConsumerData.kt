package com.example.playlistmaker.screenSearch.domain.consumer

sealed interface ConsumerData<T> {
    data class Data<T>(val value: List<T>) : ConsumerData<T>
    data class NetworkError<T>(val message: String) : ConsumerData<T>
    data class EmptyListError<T>(val message: String) : ConsumerData<T>
}