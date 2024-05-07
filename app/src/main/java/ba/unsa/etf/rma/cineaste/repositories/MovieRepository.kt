package ba.unsa.etf.rma.cineaste.repositories

import ba.unsa.etf.rma.cineaste.models.Movie
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
    private const val TMDB_API_KEY : String = "ee55b02ae6bf27432628578186bfd732"

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

                        val title = movie.getString("original_title")
                        val id = movie.getInt("id")
                        val posterPath = movie.getString("poster_path")
                        val overview = movie.getString("overview")
                        val releaseDate = movie.getString("release_date")

                        movies.add(Movie(id.toLong(), title, overview, releaseDate, null, null, posterPath, " "))

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
}