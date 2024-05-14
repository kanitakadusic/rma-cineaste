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

    private lateinit var title: TextView
    private lateinit var overview: TextView
    private lateinit var poster: ImageView
    private lateinit var backdrop: ImageView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest)

        title = findViewById(R.id.latestTitle)
        overview = findViewById(R.id.latestOverview)
        poster = findViewById(R.id.latestPoster)
        backdrop = findViewById(R.id.latestBackdrop)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(Constants.NOTIFICATION_ID_LATEST_MOVIE)

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
        title.text = movie.title
        overview.text = movie.overview

        val posterContext: Context = poster.context
        Glide.with(posterContext)
            .load(MovieRepository.POSTER_PATH + movie.posterPath)
            .centerCrop()
            .centerCrop()
            .placeholder(R.drawable.undefined)
            .error(R.drawable.undefined)
            .fallback(R.drawable.undefined)
            .into(poster)

        val backdropContext: Context = backdrop.context
        Glide.with(backdropContext)
            .load(MovieRepository.BACKDROP_PATH + movie.backdropPath)
            .centerCrop()
            .centerCrop()
            .placeholder(R.drawable.undefined)
            .error(R.drawable.undefined)
            .fallback(R.drawable.undefined)
            .into(backdrop)
    }
}