package ba.unsa.etf.rma.cineaste

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

object MovieRepository {
    private const val TMDB_API_KEY = Constants.TMDB_API_KEY

    const val POSTER_PATH = "https://image.tmdb.org/t/p/w780"
    const val BACKDROP_PATH = "https://image.tmdb.org/t/p/w500"

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

    // suspend fun getMovieActors(id: Int) {}

    // suspend fun getSimilarMovies(id: Int) {}

    suspend fun latestMovieRequest(): Result<Movie> {
        return withContext(Dispatchers.IO) {
            try {
                val movie = Movie(0, "", "", "", "", "", "")

                val urlString = "https://api.themoviedb.org/3/movie/latest?api_key=$TMDB_API_KEY"
                val url = URL(urlString)

                (url.openConnection() as? HttpURLConnection)?.run {
                    val result = this.inputStream.bufferedReader().use { it.readText() }
                    val jsonObject = JSONObject(result)

                    movie.id = jsonObject.getInt("id")
                    movie.title = jsonObject.getString("original_title")
                    movie.overview = jsonObject.getString("overview")

                    if (!jsonObject.getBoolean("adult")) {
                        movie.posterPath = jsonObject.getString("poster_path")
                        movie.backdropPath = jsonObject.getString("backdrop_path")
                    }
                }

                return@withContext Result.Success(movie)
            } catch (e: MalformedURLException) {
                return@withContext Result.Error(Exception("Cannot open HttpURLConnection"))
            } catch (e: IOException) {
                return@withContext Result.Error(Exception("Cannot read stream"))
            } catch (e: JSONException) {
                return@withContext Result.Error(Exception("Cannot parse JSON"))
            }
        }
    }
}