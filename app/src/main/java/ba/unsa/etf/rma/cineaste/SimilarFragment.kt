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

class SimilarFragment : Fragment() {
    private lateinit var similarRV: RecyclerView
    private lateinit var similarSLA: StringListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_similar, container, false)
        val intent = requireActivity().intent

        similarRV = view.findViewById(R.id.listSimilar)
        similarRV.layoutManager = LinearLayoutManager(activity)

        similarSLA = StringListAdapter(listOf())
        similarRV.adapter = similarSLA

        val extras = intent.extras
        if (extras != null && extras.containsKey("movie_id")) {
            getSimilar(extras.getInt("movie_id"))
        }

        return view
    }

    private fun getSimilar(id: Int) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)

        /*
        scope.launch {
            when (val result = MovieRepository.getSimilarMovies(id)) {
                is Response -> onSuccess(result)
                else -> onError()
            }
        }
        */
    }

    private fun onSuccess(similar: List<String>) {
        Toast.makeText(context, "Similar success", Toast.LENGTH_SHORT).show()
        similarSLA.updateStrings(similar)
    }

    private fun onError() {
        Toast.makeText(context, "Similar error", Toast.LENGTH_SHORT).show()
    }
}