package ba.unsa.etf.rma.cineaste.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.rma.cineaste.R
import ba.unsa.etf.rma.cineaste.adapters.SimpleStringAdapter
import ba.unsa.etf.rma.cineaste.repositories.MovieRepository
import ba.unsa.etf.rma.cineaste.repositories.Result
import ba.unsa.etf.rma.cineaste.utils.getSimilarMovies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SimilarFragment : Fragment() {
    private lateinit var similarRV: RecyclerView
    private lateinit var similarSSA: SimpleStringAdapter
    private var similarList: List<String> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_similar, container, false)
        val intent = requireActivity().intent
        val extras = intent.extras

        if (extras != null) {
            if (extras.containsKey("movie_title")) {
                similarList = getSimilarMovies()[extras.getString("movie_title")] ?: emptyList()
            } else if (extras.containsKey("movie_id")) {
                similar(extras.getLong("movie_id"))
            }
        }

        similarRV = view.findViewById(R.id.listSimilar)
        similarRV.layoutManager = LinearLayoutManager(activity)

        similarSSA = SimpleStringAdapter(similarList)
        similarRV.adapter = similarSSA

        return view
    }

    private fun similar(query: Long) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            when (val result = MovieRepository.similarMoviesRequest(query)) {
                is Result.Success<MutableList<String>> -> similarDone(result.data)
                else -> Toast.makeText(context, "Similar error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun similarDone(similar: MutableList<String>) {
        similarList = similar
        similarSSA.list = similar
        similarSSA.notifyDataSetChanged()
    }
}