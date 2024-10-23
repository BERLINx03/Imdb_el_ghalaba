package com.example.moviesapp.movieDetails.presentation

import com.example.moviesapp.movieList.domain.model.Movie

/**
 * @author Android Dev (Abdallah Berlin)
 */
data class MovieDetailsUiState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)