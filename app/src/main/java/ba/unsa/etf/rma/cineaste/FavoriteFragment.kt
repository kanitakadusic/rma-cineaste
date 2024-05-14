package ba.unsa.etf.rma.cineaste

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoriteFragment : Fragment() {
    private lateinit var favoriteRV: RecyclerView
    private lateinit var favoriteMLA: MovieListAdapter
    private var favoriteList = getFavoriteMovies()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        favoriteRV = view.findViewById(R.id.favoriteMovies)
        favoriteRV.layoutManager = GridLayoutManager(activity, 2)

        favoriteMLA = MovieListAdapter(arrayListOf()) { movie -> showMovieDetails(movie) }
        favoriteRV.adapter = favoriteMLA
        favoriteMLA.updateMovies(favoriteList)

        return view
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(activity, DetailsActivity::class.java).apply {
            putExtra("movie_title", movie.title)
        }

        startActivity(intent)
    }
}