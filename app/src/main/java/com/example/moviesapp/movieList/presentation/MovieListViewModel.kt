package com.example.moviesapp.movieList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.movieList.domain.repository.MovieListRepository
import com.example.moviesapp.movieList.util.Category
import com.example.moviesapp.movieList.util.Respond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
): ViewModel() {
    private val _movieListUiState = MutableStateFlow(MovieListUiState())
    val movieListUiState = _movieListUiState.asStateFlow()

    init {
        getPopularMovieList(false)
        getUpcomingMovieList(false)
    }

    fun onEvent(event: MovieListUiEvent){
        when(event){
            MovieListUiEvent.Navigate -> {
                _movieListUiState.update {
                    it.copy(
                        isPopularScreen = !it.isPopularScreen
                    )
                }
            }
            is MovieListUiEvent.Paginate -> {
                if(event.category == Category.POPULAR){
                    getPopularMovieList(true)
                }else if(event.category == Category.UPCOMING){
                    getUpcomingMovieList(true)
                }
            }
        }
    }
    private fun getPopularMovieList(forceFetchFromRemote: Boolean){
        viewModelScope.launch {
            movieListRepository.getMoviesList(
                forceFetchFromRemote,
                Category.POPULAR,
                _movieListUiState.value.popularMovieListPage
            ).collectLatest { result ->
                when(result){
                    is Respond.Error -> {
                        _movieListUiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }
                    is Respond.Loading -> {
                        _movieListUiState.update {
                            it.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                    is Respond.Success ->{
                        result.data?.let { popularMovieList ->
                            _movieListUiState.update {
                                it.copy(
                                    popularMovieList = (it.popularMovieList + popularMovieList).shuffled(),
                                    popularMovieListPage = _movieListUiState.value.popularMovieListPage + 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getUpcomingMovieList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            movieListRepository.getMoviesList(
                forceFetchFromRemote,
                Category.UPCOMING,
                _movieListUiState.value.upcomingMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Respond.Error -> {
                        _movieListUiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }

                    is Respond.Loading -> {
                        _movieListUiState.update {
                            it.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }

                    is Respond.Success -> {
                        result.data?.let { upcomingMovieList ->
                            _movieListUiState.update {
                                it.copy(
                                    upcomingMovieList = (it.upcomingMovieList + upcomingMovieList).shuffled(),
                                    upcomingMovieListPage = _movieListUiState.value.upcomingMovieListPage + 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}