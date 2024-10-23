package com.example.moviesapp.movieList.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviesapp.movieList.presentation.MovieListUiEvent
import com.example.moviesapp.movieList.presentation.MovieListUiState
import com.example.moviesapp.movieList.presentation.screens.components.MovieItem
import com.example.moviesapp.movieList.util.Category

/**
 * @author Android Dev (Abdallah Berlin)
 */
@Composable
fun UpcomingMovieListScreen(
    movieListUiState: MovieListUiState,
    navHostController: NavHostController,
    onEvent: (MovieListUiEvent) -> Unit,
) {
    if (movieListUiState.upcomingMovieList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
        ) {
            items(movieListUiState.upcomingMovieList.size) { movie ->
                MovieItem(
                    movie = movieListUiState.upcomingMovieList[movie],
                    navHostController = navHostController,
                )

                Spacer(modifier = Modifier.height(16.dp))

                if(movieListUiState.upcomingMovieList.size - 1 == movie && !movieListUiState.isLoading){
                    onEvent(MovieListUiEvent.Paginate(Category.UPCOMING))
                }
            }
        }
    }

}