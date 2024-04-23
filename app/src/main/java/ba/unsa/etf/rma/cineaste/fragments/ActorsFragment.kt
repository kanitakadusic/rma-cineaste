package ba.unsa.etf.rma.cineaste.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.rma.cineaste.R
import ba.unsa.etf.rma.cineaste.adapters.SimpleStringAdapter
import ba.unsa.etf.rma.cineaste.utils.getMovieActors

class ActorsFragment() : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_actors, container, false)
        val intent = requireActivity().intent
        val extras = intent.extras

        var actorsList = emptyList<String>()
        if (extras != null) {
            actorsList = getMovieActors()[extras.getString("movie_title")] ?: emptyList()
        }

        val actorsRV = view.findViewById<RecyclerView>(R.id.listActors)
        actorsRV.layoutManager = LinearLayoutManager(activity)
        val actorsRVSimpleAdapter = SimpleStringAdapter(actorsList)
        actorsRV.adapter = actorsRVSimpleAdapter

        return view
    }
}