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

object TmdbApiCalls {
    private const val TMDB_API_KEY = Constants.TMDB_API_KEY

    const val POSTER_PATH = "https://image.tmdb.org/t/p/w780"
    const val BACKDROP_PATH = "https://image.tmdb.org/t/p/w500"

    suspend fun searchRequest(
        query: String
    ): Result<List<Movie>> {
        return withContext(Dispatchers.IO) {
            try {
                val movies = arrayListOf<Movie>()

                val urlString = "https://api.themoviedb.org/3/search/movie?api_key=$TMDB_API_KEY&query=$query"
                val url = URL(urlString)

                (url.openConnection() as? HttpURLConnection)?.run {
                    val result: String = this.inputStream.bufferedReader().use { it.readText() }

                    val jsonObject = JSONObject(result)
                    val results = jsonObject.getJSONArray("results")

                    for (i in 0 until results.length()) {
                        val movie = results.getJSONObject(i)

                        val id = movie.getInt("id")
                        val title = movie.getString("original_title")
                        val overview = movie.getString("overview")
                        val releaseDate = movie.getString("release_date")
                        val posterPath = movie.getString("poster_path")

                        movies.add(Movie(id.toLong(), title, overview, releaseDate, null, posterPath, ""))

                        if (i == 5) break
                    }
                }

                return@withContext Result.Success(movies)
            } catch (e: MalformedURLException) {
                return@withContext Result.Error(Exception("Cannot open HttpURLConnection"))
            } catch (e: IOException) {
                return@withContext Result.Error(Exception("Cannot read stream"))
            } catch (e: JSONException) {
                return@withContext Result.Error(Exception("Cannot parse JSON"))
            }
        }
    }

    suspend fun movieDetailsRequest(
        id: Long
    ): Result<Movie> {
        return withContext(Dispatchers.IO) {
            try {
                val movie = Movie(0, "", "", "", "", "", "")

                val urlString = "https://api.themoviedb.org/3/movie/$id?api_key=$TMDB_API_KEY"
                val url = URL(urlString)

                (url.openConnection() as? HttpURLConnection)?.run {
                    val result = this.inputStream.bufferedReader().use { it.readText() }
                    val jsonObject = JSONObject(result)

                    movie.id = jsonObject.getLong("id")
                    movie.title = jsonObject.getString("original_title")
                    movie.overview = jsonObject.getString("overview")
                    movie.releaseDate = jsonObject.getString("release_date")
                    movie.homepage = jsonObject.getString("homepage")
                    movie.posterPath = jsonObject.getString("poster_path")
                    movie.backdropPath = jsonObject.getString("backdrop_path")
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

    suspend fun similarMoviesRequest(
        id: Long
    ): Result<MutableList<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val similar: MutableList<String> = mutableListOf()

                val urlString = "https://api.themoviedb.org/3/movie/$id/similar?api_key=$TMDB_API_KEY"
                val url = URL(urlString)

                (url.openConnection() as? HttpURLConnection)?.run {
                    val result = this.inputStream.bufferedReader().use { it.readText() }

                    val jsonObject = JSONObject(result)
                    val results = jsonObject.getJSONArray("results")

                    for (i in 0 until results.length()) {
                        val movie = results.getJSONObject(i)

                        val title = movie.getString("title")
                        similar.add(title)

                        if (i == 4) break
                    }
                }

                return@withContext Result.Success(similar)
            } catch (e: MalformedURLException) {
                return@withContext Result.Error(Exception("Cannot open HttpURLConnection"))
            } catch (e: IOException) {
                return@withContext Result.Error(Exception("Cannot read stream"))
            } catch (e: JSONException) {
                return@withContext Result.Error(Exception("Cannot parse JSON"))
            }
        }
    }

    suspend fun movieActorsRequest(
        id: Long
    ): Result<MutableList<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val actors: MutableList<String> = mutableListOf()

                val urlString = "https://api.themoviedb.org/3/movie/$id/credits?api_key=$TMDB_API_KEY"
                val url = URL(urlString)

                (url.openConnection() as? HttpURLConnection)?.run {
                    val result = this.inputStream.bufferedReader().use { it.readText() }

                    val jsonObject = JSONObject(result)
                    val results = jsonObject.getJSONArray("cast")

                    for (i in 0 until results.length()) {
                        val movie = results.getJSONObject(i)

                        val name = movie.getString("name")
                        actors.add(name)

                        if (i == 4) break
                    }
                }

                return@withContext Result.Success(actors)
            } catch (e: MalformedURLException) {
                return@withContext Result.Error(Exception("Cannot open HttpURLConnection"))
            } catch (e: IOException) {
                return@withContext Result.Error(Exception("Cannot read stream"))
            } catch (e: JSONException) {
                return@withContext Result.Error(Exception("Cannot parse JSON"))
            }
        }
    }

    suspend fun latestMovieRequest(): Result<Movie> {
        return withContext(Dispatchers.IO) {
            try {
                val movie = Movie(0, "", "", "", "", "", "")

                val urlString = "https://api.themoviedb.org/3/movie/latest?api_key=$TMDB_API_KEY"
                val url = URL(urlString)

                (url.openConnection() as? HttpURLConnection)?.run {
                    val result = this.inputStream.bufferedReader().use { it.readText() }
                    val jsonObject = JSONObject(result)

                    movie.id = jsonObject.getLong("id")
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