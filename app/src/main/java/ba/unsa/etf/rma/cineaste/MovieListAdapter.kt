package ba.unsa.etf.rma.cineaste

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieListAdapter (
    private var movies: List<Movie>,
    private val onItemClicked: (movie: Movie) -> Unit
) : RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    fun updateMovies(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = movies.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int
    ) {
        holder.movieTitle.text = movies[position].title

        val posterContext: Context = holder.movieImage.context
        Glide.with(posterContext)
            .load(MovieRepository.POSTER_PATH + movies[position].posterPath)
            .centerCrop()
            .placeholder(R.drawable.undefined)
            .error(R.drawable.undefined)
            .fallback(R.drawable.undefined)
            .into(holder.movieImage)

        holder.itemView.setOnClickListener { onItemClicked(movies[position]) }
    }

    inner class MovieViewHolder (
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val movieImage: ImageView = itemView.findViewById(R.id.movieImage)
        val movieTitle: TextView = itemView.findViewById(R.id.movieTitle)
    }
}