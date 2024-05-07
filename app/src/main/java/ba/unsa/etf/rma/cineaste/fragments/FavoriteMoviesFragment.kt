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
import ba.unsa.etf.rma.cineaste.utils.getFavoriteMovies

class FavoriteMoviesFragment : Fragment() {
    private lateinit var favoriteRV: RecyclerView
    private lateinit var favoriteMLA: MovieListAdapter
    private var favoriteList = getFavoriteMovies()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite_movies, container, false)

        favoriteRV = view.findViewById(R.id.favoriteMovies)
        favoriteRV.layoutManager = GridLayoutManager(activity, 2)

        favoriteMLA = MovieListAdapter(arrayListOf()) { movie -> showMovieDetails(movie) }
        favoriteRV.adapter = favoriteMLA
        favoriteMLA.updateMovies(favoriteList)

        return view
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(activity, MovieDetailActivity::class.java).apply {
            putExtra("movie_title", movie.title)
        }

        startActivity(intent)
    }
}