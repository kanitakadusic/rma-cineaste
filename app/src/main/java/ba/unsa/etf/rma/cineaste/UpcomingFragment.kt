package ba.unsa.etf.rma.cineaste

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UpcomingFragment : Fragment() {
    private lateinit var upcomingRV: RecyclerView
    private lateinit var upcomingMLA: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upcoming, container, false)

        upcomingRV = view.findViewById(R.id.upcomingMovies)
        upcomingRV.layoutManager = GridLayoutManager(activity, 2)

        upcomingMLA = MovieListAdapter(arrayListOf()) { movie -> showMovieDetails(movie) }
        upcomingRV.adapter = upcomingMLA

        getUpcoming()

        return view
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(activity, DetailsActivity::class.java).apply {
            putExtra("movie_id", movie.id)
        }

        startActivity(intent)
    }

    private fun getUpcoming() {
        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            when (val result = MovieRepository.getUpcomingMovies()) {
                is GetMoviesResponse -> onSuccess(result.movies)
                else -> onError()
            }
        }
    }

    private fun onSuccess(movies: List<Movie>) {
        Toast.makeText(context, "Upcoming success", Toast.LENGTH_SHORT).show()
        upcomingMLA.updateMovies(movies)
    }

    private fun onError() {
        Toast.makeText(context, "Upcoming error", Toast.LENGTH_SHORT).show()
    }
}