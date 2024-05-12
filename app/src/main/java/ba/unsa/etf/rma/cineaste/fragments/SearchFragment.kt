package ba.unsa.etf.rma.cineaste.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.rma.cineaste.R
import ba.unsa.etf.rma.cineaste.activities.DetailsActivity
import ba.unsa.etf.rma.cineaste.adapters.MovieListAdapter
import ba.unsa.etf.rma.cineaste.models.Movie
import ba.unsa.etf.rma.cineaste.utils.TmdbApiCalls
import ba.unsa.etf.rma.cineaste.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var searchText: EditText
    private lateinit var searchButton: AppCompatImageButton

    private lateinit var searchRV: RecyclerView
    private lateinit var searchMLA: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchText = view.findViewById(R.id.searchText)
        arguments?.getString("search")?.let { searchText.setText(it) }

        searchButton = view.findViewById(R.id.searchButton)
        searchButton.setOnClickListener { search(searchText.text.toString()) }

        searchRV = view.findViewById(R.id.searchList)
        searchRV.layoutManager = GridLayoutManager(activity, 2)

        searchMLA = MovieListAdapter(arrayListOf()) { movie -> showMovieDetails(movie) }
        searchRV.adapter = searchMLA

        return view
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(activity, DetailsActivity::class.java).apply {
            putExtra("movie_id", movie.id)
        }

        startActivity(intent)
    }

    private fun search(query: String) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            when (val result = TmdbApiCalls.searchRequest(query)) {
                is Result.Success<List<Movie>> -> searchDone(result.data)
                else -> Toast.makeText(context, "Search error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchDone(movies: List<Movie>) {
        searchMLA.updateMovies(movies)
    }
}