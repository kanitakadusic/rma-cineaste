package ba.unsa.etf.rma.cineaste

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = Constants.TMDB_API_KEY
    ): Response<GetMoviesResponse>

    @GET("search/movie")
    suspend fun getSearchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = Constants.TMDB_API_KEY
    ): Response<GetMoviesResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = Constants.TMDB_API_KEY
    ): Response<Movie>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieActors(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = Constants.TMDB_API_KEY
    ): Response<GetActorsResponse>

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = Constants.TMDB_API_KEY
    ): Response<GetMoviesResponse>
}