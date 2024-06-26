package ba.unsa.etf.rma.cineaste

import com.google.gson.annotations.SerializedName

data class GetMoviesResponse(
    @SerializedName("page") val pageNumber: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val movies: List<Movie>
)