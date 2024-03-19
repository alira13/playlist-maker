package com.example.playlistmaker.domain.models

sealed interface Resource<T> {
    data class Success<T>(val data: T) : Resource<T>
    class EmptyListError<T> : Resource<T>
    class NetworkError<T> : Resource<T>
}