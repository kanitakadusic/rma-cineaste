package ba.unsa.etf.rma.cineaste

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
}