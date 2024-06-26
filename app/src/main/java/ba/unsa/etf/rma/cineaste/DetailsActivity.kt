package ba.unsa.etf.rma.cineaste

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

    private lateinit var titleTV: TextView
    private lateinit var overviewTV: TextView
    private lateinit var releaseDateTV: TextView
    private lateinit var homepageTV: TextView
    private lateinit var posterIV: ImageView
    private lateinit var backdropIV: ImageView

    private lateinit var favoriteB: Button
    private lateinit var shareFAB: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        titleTV = findViewById(R.id.detailsTitle)
        titleTV.setOnClickListener { searchTrailer() }

        overviewTV = findViewById(R.id.detailsOverview)

        releaseDateTV = findViewById(R.id.detailsReleaseDate)

        homepageTV = findViewById(R.id.detailsWebsite)
        homepageTV.setOnClickListener { showWebsite() }

        posterIV = findViewById(R.id.detailsPoster)

        backdropIV = findViewById(R.id.detailsBackdrop)

        favoriteB = findViewById(R.id.favorite)
        favoriteB.setOnClickListener { onFavoriteButtonClicked(movie) }

        shareFAB = findViewById(R.id.shareButton)
        shareFAB.setOnClickListener { shareOverview() }

        val extras = intent.extras
        if (extras != null && extras.containsKey("movie_id")) {
            val movieId: Int = extras.getInt("movie_id")

            getDetails(movieId)
            manageFavoriteButtonText(movieId)
        } else {
            finish()
        }

        val navHost = supportFragmentManager.findFragmentById(R.id.hostFragmentDetails) as NavHostFragment
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationDetails)
        navView.setupWithNavController(navHost.navController)
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

    private fun populateDetails() {
        titleTV.text = movie.title
        overviewTV.text = movie.overview
        releaseDateTV.text = movie.releaseDate
        homepageTV.text = movie.homepage

        val posterContext: Context = posterIV.context
        Glide.with(posterContext)
            .load(Constants.POSTER_PATH + movie.posterPath)
            .centerCrop()
            .centerCrop()
            .placeholder(R.drawable.undefined)
            .error(R.drawable.undefined)
            .fallback(R.drawable.undefined)
            .into(posterIV)

        val backdropContext: Context = backdropIV.context
        Glide.with(backdropContext)
            .load(Constants.BACKDROP_PATH + movie.backdropPath)
            .centerCrop()
            .centerCrop()
            .placeholder(R.drawable.undefined)
            .error(R.drawable.undefined)
            .fallback(R.drawable.undefined)
            .into(backdropIV)
    }

    private fun getDetails(id: Int) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            when (val result = MovieRepository.getMovieDetails(id)) {
                is Movie -> onSuccess(result)
                else -> onError()
            }
        }
    }

    private fun onSuccess(movie: Movie) {
        Toast.makeText(this, "Details success", Toast.LENGTH_SHORT).show()
        this.movie = movie
        populateDetails()
    }

    private fun onError() {
        Toast.makeText(this, "Details error", Toast.LENGTH_SHORT).show()
    }

    private fun onFavoriteButtonClicked(movie: Movie) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        val activityContext: Context = this

        scope.launch {
            when (favoriteB.text.toString()) {
                getString(R.string.add_to_favorites) -> {
                    when (MovieRepository.addMovieToFavorites(activityContext, movie)) {
                        true -> onAddSuccess()
                        false -> Toast.makeText(activityContext, "Add error", Toast.LENGTH_SHORT).show()
                    }
                }

                getString(R.string.delete_from_favorites) -> {
                    when (MovieRepository.deleteMovieFromFavorites(activityContext, movie)) {
                        true -> onDeleteSuccess()
                        false -> Toast.makeText(activityContext, "Delete error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun onAddSuccess() {
        Toast.makeText(this, "Add success", Toast.LENGTH_SHORT).show()
        favoriteB.setText(R.string.delete_from_favorites)
    }

    private fun onDeleteSuccess() {
        Toast.makeText(this, "Delete success", Toast.LENGTH_SHORT).show()
        favoriteB.setText(R.string.add_to_favorites)
    }

    private fun manageFavoriteButtonText(id: Int) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        val activityContext: Context = this

        scope.launch {
            when (MovieRepository.findMovieById(activityContext, id)) {
                is Movie -> favoriteB.setText(R.string.delete_from_favorites)
                else -> favoriteB.setText(R.string.add_to_favorites)
            }
        }
    }
}