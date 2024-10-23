package com.example.moviesapp.movieList.di

import android.app.Application
import androidx.room.Room
import com.example.moviesapp.movieList.data.local.MovieDatabase
import com.example.moviesapp.movieList.data.remote.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = okhttp3.OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun provideMovieApi(): MovieApi{
        return Retrofit.Builder()
            .baseUrl(MovieApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieDB(app: Application): MovieDatabase{
        return Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            "movie_db"
        ).build()
    }
}
