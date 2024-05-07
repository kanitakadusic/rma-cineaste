package ba.unsa.etf.rma.cineaste.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.rma.cineaste.R
import ba.unsa.etf.rma.cineaste.adapters.SimpleStringAdapter
import ba.unsa.etf.rma.cineaste.utils.getSimilarMovies

class SimilarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_similar, container, false)
        val intent = requireActivity().intent
        val extras = intent.extras

        var similarList = emptyList<String>()
        if (extras != null) {
            similarList = getSimilarMovies()[extras.getString("movie_title")] ?: emptyList()
        }

        val similarRV = view.findViewById<RecyclerView>(R.id.listSimilar)
        similarRV.layoutManager = LinearLayoutManager(activity)
        val similarRVSimpleAdapter = SimpleStringAdapter(similarList)
        similarRV.adapter = similarRVSimpleAdapter

        return view
    }
}