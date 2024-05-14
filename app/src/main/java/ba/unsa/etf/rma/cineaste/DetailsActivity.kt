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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailsActivity : AppCompatActivity() {
    private lateinit var movie: Movie

    private lateinit var title: TextView
    private lateinit var overview: TextView
    private lateinit var releaseDate: TextView
    private lateinit var website: TextView
    private lateinit var poster: ImageView
    private lateinit var backdrop: ImageView

    private lateinit var share: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        title = findViewById(R.id.detailsTitle)
        overview = findViewById(R.id.detailsOverview)
        releaseDate = findViewById(R.id.detailsReleaseDate)
        website = findViewById(R.id.detailsWebsite)
        poster = findViewById(R.id.detailsPoster)
        backdrop = findViewById(R.id.detailsBackdrop)

        share = findViewById(R.id.shareButton)

        val extras = intent.extras
        if (extras != null) {
            if (extras.containsKey("movie_title")) {
                movie = getMovieByTitle(extras.getString("movie_title",""))
                populateDetails()
            } else if (extras.containsKey("movie_id")) {
                details(extras.getLong("movie_id"))
            }
        } else {
            finish()
        }

        title.setOnClickListener { searchTrailer() }
        website.setOnClickListener { showWebsite() }
        share.setOnClickListener { shareOverview() }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragmentDetails) as NavHostFragment
        val navController = navHostFragment.navController
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationDetails)
        navView.setupWithNavController(navController)
    }

    private fun getMovieByTitle(name: String): Movie {
        val movies: ArrayList<Movie> = arrayListOf()
        movies.addAll(getRecentMovies())
        movies.addAll(getFavoriteMovies())

        val movie = movies.find { movie -> name == movie.title }
        return movie ?: Movie(0, "", "", "", "", "", "")
    }

    private fun populateDetails() {
        title.text = movie.title
        overview.text = movie.overview
        releaseDate.text = movie.releaseDate
        website.text = movie.homepage

        val posterContext: Context = poster.context
        Glide.with(posterContext)
            .load(TmdbApiCalls.POSTER_PATH + movie.posterPath)
            .centerCrop()
            .centerCrop()
            .placeholder(R.drawable.undefined)
            .error(R.drawable.undefined)
            .fallback(R.drawable.undefined)
            .into(poster)

        val backdropContext: Context = backdrop.context
        Glide.with(backdropContext)
            .load(TmdbApiCalls.BACKDROP_PATH + movie.backdropPath)
            .centerCrop()
            .centerCrop()
            .placeholder(R.drawable.undefined)
            .error(R.drawable.undefined)
            .fallback(R.drawable.undefined)
            .into(backdrop)
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

    private fun details(query: Long) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            when (val result = TmdbApiCalls.movieDetailsRequest(query)) {
                is Result.Success<Movie> -> detailsDone(result.data)
                else -> Toast.makeText(baseContext, "Details error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun detailsDone(movie: Movie) {
        this.movie = movie
        populateDetails()
    }
}