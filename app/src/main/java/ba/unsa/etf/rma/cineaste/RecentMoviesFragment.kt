package ba.unsa.etf.rma.cineaste

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class RecentMoviesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_recent_movies, container, false)

    companion object {
        fun newInstance(): RecentMoviesFragment = RecentMoviesFragment()
    }
}