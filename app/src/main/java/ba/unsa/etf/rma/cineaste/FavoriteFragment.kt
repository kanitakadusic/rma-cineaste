package ba.unsa.etf.rma.cineaste

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private lateinit var favoriteRV: RecyclerView
    private lateinit var favoriteMLA: MovieListAdapter

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

        context?.let { getFavorite(it) }

        return view
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(activity, DetailsActivity::class.java).apply {
            putExtra("movie_id", movie.id)
        }

        startActivity(intent)
    }

    private fun getFavorite(context: Context) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            val result = MovieRepository.getFavoriteMovies(context)

            Toast.makeText(context, "Favorite success", Toast.LENGTH_SHORT).show()
            favoriteMLA.updateMovies(result)
        }
    }
}