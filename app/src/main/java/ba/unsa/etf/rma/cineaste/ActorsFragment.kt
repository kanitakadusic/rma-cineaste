package ba.unsa.etf.rma.cineaste

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ActorsFragment : Fragment() {
    private lateinit var actorsRV: RecyclerView
    private lateinit var actorsSSA: SimpleStringAdapter
    private var actorsList: List<String> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_actors, container, false)
        val intent = requireActivity().intent
        val extras = intent.extras

        if (extras != null) {
            if (extras.containsKey("movie_title")) {
                actorsList = getMovieActors()[extras.getString("movie_title")] ?: emptyList()
            } else if (extras.containsKey("movie_id")) {
                actors(extras.getLong("movie_id"))
            }
        }

        actorsRV = view.findViewById(R.id.listActors)
        actorsRV.layoutManager = LinearLayoutManager(activity)

        actorsSSA = SimpleStringAdapter(actorsList)
        actorsRV.adapter = actorsSSA

        return view
    }

    private fun actors(query: Long) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            when (val result = TmdbApiCalls.movieActorsRequest(query)) {
                is Result.Success<MutableList<String>> -> actorsDone(result.data)
                else -> Toast.makeText(context, "Actors error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun actorsDone(actors: MutableList<String>) {
        actorsList = actors
        actorsSSA.list = actors
        actorsSSA.notifyDataSetChanged()
    }
}