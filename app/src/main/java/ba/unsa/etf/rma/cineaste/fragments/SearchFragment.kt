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
import ba.unsa.etf.rma.cineaste.activities.MovieDetailActivity
import ba.unsa.etf.rma.cineaste.adapters.MovieListAdapter
import ba.unsa.etf.rma.cineaste.models.Movie
import ba.unsa.etf.rma.cineaste.repositories.MovieRepository
import ba.unsa.etf.rma.cineaste.repositories.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var searchText: EditText
    private lateinit var searchButton: AppCompatImageButton
    private lateinit var searchMovies: RecyclerView
    private lateinit var searchMoviesAdapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchText = view.findViewById(R.id.searchText)
        arguments?.getString("search")?.let { searchText.setText(it) }

        searchButton = view.findViewById(R.id.searchButton)
        searchButton.setOnClickListener { onClick() }

        searchMovies = view.findViewById(R.id.searchList)
        searchMovies.layoutManager = GridLayoutManager(activity, 2)

        searchMoviesAdapter = MovieListAdapter(arrayListOf()) { movie -> showMovieDetails(movie) }
        searchMovies.adapter = searchMoviesAdapter

        return view
    }

    private fun onClick() {
        Toast.makeText(context, "Search start", Toast.LENGTH_SHORT).show()
        search(searchText.text.toString())
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(activity, MovieDetailActivity::class.java).apply {
            putExtra("movie_id", movie.id)
        }

        startActivity(intent)
    }

    private fun search(query: String) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            when (val result = MovieRepository.searchRequest(query)) {
                is Result.Success<List<Movie>> -> searchDone(result.data)
                else -> onError()
            }
        }
    }

    private fun searchDone(movies: List<Movie>) {
        Toast.makeText(context, "Search done", Toast.LENGTH_SHORT).show()
        searchMoviesAdapter.updateMovies(movies)
    }

    private fun onError() {
        Toast.makeText(context, "Search error", Toast.LENGTH_SHORT).show()
    }
}