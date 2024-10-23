package com.example.moviesapp.movieList.presentation

import com.example.moviesapp.movieList.domain.model.Movie

data class MovieListUiState(
    val isLoading: Boolean = false,

    val popularMovieList: List<Movie> = emptyList(),
    val upcomingMovieList: List<Movie> = emptyList(),

    val popularMovieListPage: Int = 1,
    val upcomingMovieListPage: Int = 1,

    val isPopularScreen: Boolean = true,


)