package ba.unsa.etf.rma.cineaste

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var searchET: EditText
    private lateinit var searchIB: AppCompatImageButton

    private lateinit var searchRV: RecyclerView
    private lateinit var searchMLA: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchET = view.findViewById(R.id.searchText)
        arguments?.getString("search")?.let { searchET.setText(it) }

        searchIB = view.findViewById(R.id.searchButton)
        searchIB.setOnClickListener { getSearch(searchET.text.toString()) }

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

    private fun getSearch(title: String) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            when (val result = MovieRepository.getSearchMovies(title)) {
                is GetMoviesResponse -> onSuccess(result.movies)
                else -> onError()
            }
        }
    }

    private fun onSuccess(movies: List<Movie>) {
        Toast.makeText(context, "Search success", Toast.LENGTH_SHORT).show()
        searchMLA.updateMovies(movies)
    }

    private fun onError() {
        Toast.makeText(context, "Search error", Toast.LENGTH_SHORT).show()
    }
}