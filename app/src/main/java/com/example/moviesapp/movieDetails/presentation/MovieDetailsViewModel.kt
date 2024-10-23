package com.example.moviesapp.movieDetails.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.movieList.domain.repository.MovieListRepository
import com.example.moviesapp.movieList.util.Respond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepository: MovieListRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val movieId = savedStateHandle.get<Int>("movieId")

    private val _movieDetails = MutableStateFlow(MovieDetailsUiState())
    val movieDetails = _movieDetails.asStateFlow()

    init {
        getMovieDetails(movieId ?: -1)
    }

    private fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _movieDetails.update {
                it.copy(
                    isLoading = true
                )
            }

            movieRepository.getMovie(movieId).collectLatest { result ->
                when (result) {
                    is Respond.Error -> {
                        _movieDetails.update {
                            it.copy(
                                isLoading = false,
                            )
                        }
                    }

                    is Respond.Loading -> {
                        _movieDetails.update {
                            it.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }

                    is Respond.Success -> {
                        result.data?.let { movie ->
                            _movieDetails.update {
                                it.copy(
                                    movie = movie
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}