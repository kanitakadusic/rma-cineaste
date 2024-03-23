package ba.unsa.etf.rma.cineaste

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var favoriteMovies: RecyclerView
    private lateinit var favoriteMoviesAdapter: MovieListAdapter
    private var favoriteMoviesList = getFavoriteMovies()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        favoriteMovies = findViewById(R.id.favoriteMovies)
        favoriteMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        favoriteMoviesAdapter = MovieListAdapter(listOf())
        favoriteMovies.adapter = favoriteMoviesAdapter
        favoriteMoviesAdapter.updateMovies(favoriteMoviesList)
    }
}