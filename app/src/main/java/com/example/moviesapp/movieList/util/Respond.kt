package com.example.moviesapp.movieList.util

sealed class Respond<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : Respond<T>(data)
    class Error<T>(message: String, data: T? = null) : Respond<T>(data, message)
    class Loading<T>(val isLoading: Boolean = true) : Respond<T>(null)

}