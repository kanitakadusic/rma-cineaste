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
    private lateinit var actorsSLA: StringListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_actors, container, false)
        val intent = requireActivity().intent

        actorsRV = view.findViewById(R.id.listActors)
        actorsRV.layoutManager = LinearLayoutManager(activity)

        actorsSLA = StringListAdapter(listOf())
        actorsRV.adapter = actorsSLA

        val extras = intent.extras
        if (extras != null && extras.containsKey("movie_id")) {
            getActors(extras.getInt("movie_id"))
        }

        return view
    }

    private fun getActors(id: Int) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            when (val result = MovieRepository.getMovieActors(id)) {
                is GetActorsResponse -> onSuccess(result.actors)
                else -> onError()
            }
        }
    }

    private fun onSuccess(actors: List<Actor>) {
        Toast.makeText(context, "Actors success", Toast.LENGTH_SHORT).show()
        actorsSLA.updateStrings(actors.map { it.name })
    }

    private fun onError() {
        Toast.makeText(context, "Actors error", Toast.LENGTH_SHORT).show()
    }
}