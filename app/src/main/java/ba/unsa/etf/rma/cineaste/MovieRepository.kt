package ba.unsa.etf.rma.cineaste

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MovieRepository {
    suspend fun getUpcomingMovies(): GetMoviesResponse? {
        return withContext(Dispatchers.IO) {
            val response = ApiAdapter.retrofit.getUpcomingMovies()
            return@withContext response.body()
        }
    }

    suspend fun getSearchMovies(title: String): GetMoviesResponse? {
        return withContext(Dispatchers.IO) {
            val response = ApiAdapter.retrofit.getSearchMovies(title)
            return@withContext response.body()
        }
    }

    suspend fun getMovieDetails(id: Int): Movie? {
        return withContext(Dispatchers.IO) {
            val response = ApiAdapter.retrofit.getMovieDetails(id)
            return@withContext response.body()
        }
    }

    suspend fun getMovieActors(id: Int): GetActorsResponse? {
        return withContext(Dispatchers.IO) {
            val response = ApiAdapter.retrofit.getMovieActors(id)
            return@withContext response.body()
        }
    }

    suspend fun getSimilarMovies(id: Int): GetMoviesResponse? {
        return withContext(Dispatchers.IO) {
            val response = ApiAdapter.retrofit.getSimilarMovies(id)
            return@withContext response.body()
        }
    }

    suspend fun getLatestMovie(): Movie? {
        return withContext(Dispatchers.IO) {
            val response = ApiAdapter.retrofit.getLatestMovie()
            return@withContext response.body()
        }
    }

    suspend fun getFavoriteMovies(context: Context): List<Movie> {
        return withContext(Dispatchers.IO) {
            try {
                val database = MovieDatabase.getInstance(context)
                return@withContext database.movieDao().readAll()
            } catch (e: Exception) {
                return@withContext listOf()
            }
        }
    }

    suspend fun addMovieToFavorites(context: Context, movie: Movie): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val database = MovieDatabase.getInstance(context)
                database.movieDao().insertAll(movie)
                return@withContext true
            } catch (e: Exception) {
                return@withContext false
            }
        }
    }

    suspend fun deleteMovieFromFavorites(context: Context, movie: Movie): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val database = MovieDatabase.getInstance(context)
                database.movieDao().deleteAll(movie)
                return@withContext true
            } catch (e: Exception) {
                return@withContext false
            }
        }
    }

    suspend fun findMovieById(context: Context, id: Int): Movie? {
        return withContext(Dispatchers.IO) {
            try {
                val database = MovieDatabase.getInstance(context)
                return@withContext database.movieDao().findById(id)
            } catch (e: Exception) {
                return@withContext null
            }
        }
    }
}