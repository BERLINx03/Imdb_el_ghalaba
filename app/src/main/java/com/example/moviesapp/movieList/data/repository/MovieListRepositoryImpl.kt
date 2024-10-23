package com.example.moviesapp.movieList.data.repository

import com.example.moviesapp.movieList.data.local.MovieDatabase
import com.example.moviesapp.movieList.data.mapper.toMovie
import com.example.moviesapp.movieList.data.mapper.toMovieEntity
import com.example.moviesapp.movieList.data.remote.MovieApi
import com.example.moviesapp.movieList.domain.model.Movie
import com.example.moviesapp.movieList.domain.repository.MovieListRepository
import com.example.moviesapp.movieList.util.Respond
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDB: MovieDatabase
)
    : MovieListRepository {
    override suspend fun getMoviesList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int,
    ): Flow<Respond<List<Movie>>> {
        return flow {

            emit(Respond.Loading(true))

            val localMovieList = movieDB.movieDao.getMovieListByCategory(category)

            val loadFromDB = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (loadFromDB){
                emit(
                    Respond.Success(
                        data = localMovieList.map { it.toMovie(category) }
                    )
                )

                emit(Respond.Loading(false))
                return@flow
            }

            val remoteMovieList = try {
                movieApi.getMoviesList(category,page)
            } catch (e: IOException){
                e.printStackTrace()
                emit(Respond.Error("Couldn't load data"))
                return@flow
            } catch (e: HttpException){
                e.printStackTrace()
                emit(Respond.Error("Couldn't load data"))
                return@flow
            } catch (e: Exception){
                e.printStackTrace()
                emit(Respond.Error("Couldn't load data"))
                return@flow
            }

            val movieListEntities = remoteMovieList.results.let { listMovieDto ->
                listMovieDto.map { movieDto ->
                    movieDto.toMovieEntity(category)
                }
            }

            movieDB.movieDao.upsertMovieList(movieListEntities)

            emit(Respond.Success(
                data = movieListEntities.map { it.toMovie(category) }
            ))
            emit(Respond.Loading(false))
        }
    }

    override suspend fun getMovie(id: Int): Flow<Respond<Movie>> {
        return flow {
            emit(Respond.Loading(true))

            val movieEntity = movieDB.movieDao.getMovieById(id)

            if(movieEntity != null){
                emit(Respond.Success(
                    data = movieEntity.toMovie(movieEntity.category)
                ))
                emit(Respond.Loading(false))
                return@flow
            }

            emit(Respond.Error("No Movie Found"))
            emit(Respond.Loading(false))

        }
    }
}