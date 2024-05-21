package ba.unsa.etf.rma.cineaste

object Constants {
    const val TMDB_API_KEY: String = BuildConfig.TMDB_API_KEY

    const val POSTER_PATH = "https://image.tmdb.org/t/p/w780"
    const val BACKDROP_PATH = "https://image.tmdb.org/t/p/w500"

    const val NOTIFICATION_ID_LATEST_MOVIE_ON_CREATE = 100
    const val NOTIFICATION_ID_LATEST_MOVIE_ON_START = 101

    const val CHANNEL_ID_LATEST_MOVIE = "LATEST MOVIE SERVICE CHANNEL"
}