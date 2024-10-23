package com.example.moviesapp.movieList.presentation.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.moviesapp.movieList.data.remote.MovieApi
import com.example.moviesapp.movieList.domain.model.Movie
import com.example.moviesapp.movieList.presentation.MovieListUiEvent
import com.example.moviesapp.movieList.util.RatingBar
import com.example.moviesapp.movieList.util.Screen
import com.example.moviesapp.movieList.util.getAverageColor

/**
 * @author Android Dev (Abdallah Berlin)
 */
@Composable
fun MovieItem(
    movie: Movie,
    navHostController: NavHostController,
) {
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + movie.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val defaultColor = MaterialTheme.colorScheme.secondaryContainer
    var dominantColor by remember {
        mutableStateOf(defaultColor)
    }

    Column(
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(18))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        defaultColor,
                        dominantColor
                    )
                )
            )
            .clickable {
                navHostController.navigate(Screen.Details.route + "/${movie.id}")
            }
    ) {
        when (imageState){
            is AsyncImagePainter.State.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .height(250.dp)
                        .clip(RoundedCornerShape(18))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(70.dp),
                        imageVector = Icons.Outlined.ImageNotSupported,
                        contentDescription = movie.title
                    )
                }


                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = movie.title,
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontSize = 15.sp,
                    maxLines = 1
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, bottom = 12.dp, top = 4.dp)
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
            }
            is AsyncImagePainter.State.Loading -> {

            }
            is AsyncImagePainter.State.Success -> {

                dominantColor = getAverageColor(
                    imageBitmap = imageState.result.drawable.toBitmap().asImageBitmap()
                )

                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .height(250.dp)
                        .clip(RoundedCornerShape(18)),
                    painter = imageState.painter ,
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = movie.title,
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontSize = 15.sp,
                    maxLines = 1
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, bottom = 12.dp, top = 4.dp)
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
            }

            AsyncImagePainter.State.Empty -> {

            }
        }
    }
}
