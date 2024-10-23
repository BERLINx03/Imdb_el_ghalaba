package com.example.moviesapp.movieList.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key.Companion.I
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviesapp.R
import com.example.moviesapp.movieList.presentation.MovieListUiEvent
import com.example.moviesapp.movieList.presentation.MovieListViewModel
import com.example.moviesapp.movieList.util.Screen
import com.example.moviesapp.setAppLanguage
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val moviesListViewModel = hiltViewModel<MovieListViewModel>()

    val movieListUiState by moviesListViewModel.movieListUiState.collectAsState()

    /**
     * navController for the button navigation
     * */
    val bottomNavController = rememberNavController()

    val currentLanguage = Locale.getDefault().language
    Scaffold(bottomBar = {
        BottomNavigationBar(
            bottomNavController = bottomNavController, onEvent = moviesListViewModel::onEvent
        )
    }, topBar = {
        TopAppBar(
            title = {
                Text(
                    text = if (movieListUiState.isPopularScreen) stringResource(R.string.popular)
                    else stringResource(R.string.upcoming), fontSize = 20.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    if(currentLanguage == "ar")
                        setAppLanguage(context,"en")
                    else
                        setAppLanguage(context,"ar")
                }) {
                    Icon(imageVector = Icons.Default.Abc, contentDescription = "change app language")
                }
            }
            , modifier = Modifier.shadow(5.dp), colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                titleContentColor = MaterialTheme.colorScheme.onBackground
            )
        )
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = Screen.PopularMovieList.route
            ) {

                composable(Screen.PopularMovieList.route) {
                    PopularMovieListScreen(
                        movieListUiState = movieListUiState,
                        navHostController = navHostController,
                        onEvent = moviesListViewModel::onEvent
                    )
                }

                composable(Screen.UpcomingMovieList.route) {
                    UpcomingMovieListScreen(
                        movieListUiState = movieListUiState,
                        navHostController = navHostController,
                        onEvent = moviesListViewModel::onEvent
                    )
                }
            }
        }
    }
}


@Composable
fun BottomNavigationBar(
    bottomNavController: NavHostController,
    onEvent: (MovieListUiEvent) -> Unit,
) {

    val moviesListViewModel = hiltViewModel<MovieListViewModel>()

    val movieListUiState by moviesListViewModel.movieListUiState.collectAsState()

    val items = listOf<BottomItem>(
        BottomItem(
            title = stringResource(R.string.popular), icon = Icons.Default.Movie
        ), BottomItem(
            title = stringResource(R.string.upcoming), icon = Icons.Default.Upcoming
        )
    )

    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        Row(modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)) {
            items.forEachIndexed { index, bottomItem ->
                NavigationBarItem(selected = selectedItem == index, onClick = {
                    selectedItem = index
                    when (selectedItem) {
                        0 -> {
                            if(movieListUiState.isPopularScreen){
                                onEvent(MovieListUiEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screen.UpcomingMovieList.route)
                            }
                        }

                        1 -> {
                            if(!movieListUiState.isPopularScreen) {
                                onEvent(MovieListUiEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screen.PopularMovieList.route)
                            }
                        }
                    }
                }, icon = {
                    Icon(
                        imageVector = bottomItem.icon,
                        contentDescription = bottomItem.title,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }, label = {
                    Text(
                        text = bottomItem.title, color = MaterialTheme.colorScheme.onBackground
                    )
                })
            }
        }
    }
}


data class BottomItem(
    val title: String,
    val icon: ImageVector,
)