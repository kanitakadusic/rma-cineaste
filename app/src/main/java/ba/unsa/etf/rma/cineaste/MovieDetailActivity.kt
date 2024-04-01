package ba.unsa.etf.rma.cineaste

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var movie: Movie
    private lateinit var title: TextView
    private lateinit var overview: TextView
    private lateinit var releaseDate: TextView
    private lateinit var genre: TextView
    private lateinit var website: TextView
    private lateinit var poster: ImageView
    private lateinit var share: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        title = findViewById(R.id.movie_title)
        overview = findViewById(R.id.movie_overview)
        releaseDate = findViewById(R.id.movie_release_date)
        genre = findViewById(R.id.movie_genre)
        website = findViewById(R.id.movie_website)
        poster = findViewById(R.id.movie_poster)
        share = findViewById(R.id.shareButton)

        val extras = intent.extras
        if (extras != null) {
            movie = getMovieByTitle(extras.getString("movie_title",""))
            populateDetails()
        } else {
            finish()
        }

        title.setOnClickListener { searchTrailer() }

        website.setOnClickListener { showWebsite() }

        share.setOnClickListener { shareOverview() }
    }

    private fun getMovieByTitle(name: String): Movie {
        val movies: ArrayList<Movie> = arrayListOf()
        movies.addAll(getRecentMovies())
        movies.addAll(getFavoriteMovies())

        val movie = movies.find { movie -> name == movie.title }
        return movie ?: Movie(0, "Test", "Test", "Test", "Test", "Test")
    }

    private fun populateDetails() {
        title.text = movie.title
        overview.text = movie.overview
        releaseDate.text = movie.releaseDate
        genre.text = movie.genre
        website.text = movie.homepage

        val context: Context = poster.context

        var id: Int = context.resources
            .getIdentifier(movie.genre, "drawable", context.packageName)
        if (id == 0) id = context.resources
            .getIdentifier("undefined", "drawable", context.packageName)

        poster.setImageResource(id)
    }

    private fun searchTrailer() {
        val searchIntent: Intent = Intent().apply {
            action = Intent.ACTION_WEB_SEARCH
            putExtra(SearchManager.QUERY, movie.title + " trailer")
        }

        try {
            startActivity(searchIntent)
        } catch (e: ActivityNotFoundException) {
            Log.v("Error", e.toString())
        }
    }

    private fun showWebsite() {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(movie.homepage))

        try {
            startActivity(webIntent)
        } catch (e: ActivityNotFoundException) {
            Log.v("Error", e.toString())
        }
    }

    private fun shareOverview() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, movie.overview)
            type = "text/plain"
        }

        try {
            startActivity(Intent.createChooser(sendIntent, "Send to"))
        } catch (e: ActivityNotFoundException) {
            Log.v("Error", e.toString())
        }
    }
}