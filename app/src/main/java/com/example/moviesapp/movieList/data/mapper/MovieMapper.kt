package com.example.moviesapp.movieList.data.mapper

import com.example.moviesapp.movieList.data.local.MovieEntity
import com.example.moviesapp.movieList.data.remote.respond.MovieDto
import com.example.moviesapp.movieList.domain.model.Movie

fun MovieDto.toMovieEntity(category: String): MovieEntity {
    return MovieEntity(
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        original_language = original_language ?: "",
        overview = overview ?: "",
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        video = video ?: false,
        vote_average = vote_average ?: 0.0,
        vote_count = vote_count ?: 0,
        category = category,
        id = id ?: -1,
        original_title = original_title ?: "",
        popularity = popularity ?: 0.0,
        genre_ids = genre_ids?.joinToString(",") ?: "-1,-2"
    )
}
fun MovieEntity.toMovie(
    category: String
): Movie {
    return Movie(
        poster_path =  poster_path,
        overview = overview,
        release_date = release_date,
        title = title,
        id = id,
        category = category,
        vote_average = vote_average,
        vote_count = vote_count,
        adult = adult,
        backdrop_path = backdrop_path,
        original_language = original_language,
        original_title = original_title,
        popularity = popularity,
        video = video,
        genre_ids = genre_ids.split(",").mapNotNull{ it.toIntOrNull() },
    )
}