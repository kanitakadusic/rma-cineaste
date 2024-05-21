package ba.unsa.etf.rma.cineaste

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class LatestActivity : AppCompatActivity() {
    private var movie = Movie(0, "", "", "", "", "", "")

    private lateinit var titleTV: TextView
    private lateinit var overviewTV: TextView
    private lateinit var posterIV: ImageView
    private lateinit var backdropIV: ImageView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest)

        titleTV = findViewById(R.id.latestTitle)
        overviewTV = findViewById(R.id.latestOverview)
        posterIV = findViewById(R.id.latestPoster)
        backdropIV = findViewById(R.id.latestBackdrop)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(Constants.NOTIFICATION_ID_LATEST_MOVIE_ON_START)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (intent?.getParcelableExtra("movie", Movie::class.java) !== null) {
                movie = intent?.getParcelableExtra("movie", Movie::class.java)!!
                populateDetails()
            }
        } else {
            @Suppress("DEPRECATION")
            if (intent?.getParcelableExtra<Movie>("movie") !== null) {
                movie = intent?.getParcelableExtra("movie")!!
                populateDetails()
            }
        }
    }

    private fun populateDetails() {
        titleTV.text = movie.title
        overviewTV.text = movie.overview

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
}