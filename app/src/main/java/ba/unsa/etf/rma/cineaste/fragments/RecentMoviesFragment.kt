package ba.unsa.etf.rma.cineaste.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.rma.cineaste.models.Movie
import ba.unsa.etf.rma.cineaste.activities.MovieDetailActivity
import ba.unsa.etf.rma.cineaste.adapters.MovieListAdapter
import ba.unsa.etf.rma.cineaste.R
import ba.unsa.etf.rma.cineaste.utils.getRecentMovies

class RecentMoviesFragment : Fragment() {
    private lateinit var recentMovies: RecyclerView
    private lateinit var recentMoviesAdapter: MovieListAdapter
    private var recentMoviesList = getRecentMovies()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recent_movies, container, false)

        recentMovies = view.findViewById(R.id.recentMovies)
        recentMovies.layoutManager = GridLayoutManager(activity, 2)

        recentMoviesAdapter = MovieListAdapter(arrayListOf()) { movie -> showMovieDetails(movie) }
        recentMovies.adapter = recentMoviesAdapter
        recentMoviesAdapter.updateMovies(recentMoviesList)

        return view
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(activity, MovieDetailActivity::class.java).apply {
            putExtra("movie_title", movie.title)
        }

        startActivity(intent)
    }
}