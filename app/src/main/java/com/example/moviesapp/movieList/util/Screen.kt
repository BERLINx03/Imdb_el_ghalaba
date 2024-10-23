package com.example.moviesapp.movieList.util

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object PopularMovieList : Screen("popularMovie")
    data object UpcomingMovieList : Screen("upcomingMovie")
    data object Details : Screen("details")
}