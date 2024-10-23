package com.example.moviesapp.movieList.domain.repository

import com.example.moviesapp.movieList.domain.model.Movie
import com.example.moviesapp.movieList.util.Respond
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    suspend fun getMoviesList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Respond<List<Movie>>>

    suspend fun getMovie(id: Int): Flow<Respond<Movie>>
}