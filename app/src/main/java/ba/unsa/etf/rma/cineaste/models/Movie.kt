package ba.unsa.etf.rma.cineaste.models

data class Movie (
    val id: Long,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val homepage: String?,
    val genre: String?,
    val posterPath: String,
    val backdropPath: String
)