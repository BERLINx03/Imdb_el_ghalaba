package com.example.moviesapp.movieDetails.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.moviesapp.R
import com.example.moviesapp.movieDetails.presentation.MovieDetailsViewModel
import com.example.moviesapp.movieList.data.remote.MovieApi
import com.example.moviesapp.movieList.util.RatingBar

/**
 * @author Android Dev (Abdallah Berlin)
 */
@Composable
fun DetailsScreen() {
    val movieDetailsViewModel = hiltViewModel<MovieDetailsViewModel>()
    val movieDetails by movieDetailsViewModel.movieDetails.collectAsState()

    val backDropImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + movieDetails.movie?.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val posterImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + movieDetails.movie?.poster_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        when (backDropImageState) {
            is AsyncImagePainter.State.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(70.dp),
                        imageVector = Icons.Outlined.ImageNotSupported,
                        contentDescription = movieDetails.movie?.title
                    )
                }
            }

            is AsyncImagePainter.State.Loading -> {

            }

            is AsyncImagePainter.State.Success -> {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    painter = backDropImageState.painter,
                    contentDescription = movieDetails.movie?.title,
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(160.dp, 240.dp)
                    ) {
                        if (posterImageState is AsyncImagePainter.State.Error) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(70.dp),
                                    imageVector = Icons.Outlined.ImageNotSupported,
                                    contentDescription = movieDetails.movie?.title
                                )
                            }
                        }
                        if (posterImageState is AsyncImagePainter.State.Success) {
                            Image(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp)),
                                painter = posterImageState.painter,
                                contentDescription = movieDetails.movie?.title,
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    movieDetails.movie?.let { movie ->
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = movie.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                            )



                            Row(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                            ) {
                                RatingBar(
                                    starsModifier = Modifier.size(18.dp),
                                    rating = movie.vote_average / 2
                                )

                                Text(
                                    text = movie.vote_average.toString().take(3),
                                    modifier = Modifier.padding(start = 4.dp),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.LightGray,
                                    fontSize = 13.sp,
                                    maxLines = 1
                                )
                            }

                            Text(
                                text = stringResource(R.string.language) + movie.original_language,
                                modifier = Modifier.padding(16.dp)
                            )


                            Text(
                                text = stringResource(R.string.release_date) + movie.release_date,
                                modifier = Modifier.padding(16.dp)
                            )


                            Text(
                                text = movie.vote_count.toString() + stringResource(R.string.votes),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }


                Text(
                    text = stringResource(R.string.overview),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = movieDetails.movie?.overview ?: "",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(32.dp))

            }

            AsyncImagePainter.State.Empty -> {

            }
        }
    }
}