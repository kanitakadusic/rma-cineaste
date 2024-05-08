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
import ba.unsa.etf.rma.cineaste.activities.DetailsActivity
import ba.unsa.etf.rma.cineaste.adapters.MovieListAdapter
import ba.unsa.etf.rma.cineaste.R
import ba.unsa.etf.rma.cineaste.utils.getRecentMovies

class UpcomingFragment : Fragment() {
    private lateinit var recentRV: RecyclerView
    private lateinit var recentMLA: MovieListAdapter
    private var recentList = getRecentMovies()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upcoming, container, false)

        recentRV = view.findViewById(R.id.upcomingMovies)
        recentRV.layoutManager = GridLayoutManager(activity, 2)

        recentMLA = MovieListAdapter(arrayListOf()) { movie -> showMovieDetails(movie) }
        recentRV.adapter = recentMLA
        recentMLA.updateMovies(recentList)

        return view
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(activity, DetailsActivity::class.java).apply {
            putExtra("movie_title", movie.title)
        }

        startActivity(intent)
    }
}