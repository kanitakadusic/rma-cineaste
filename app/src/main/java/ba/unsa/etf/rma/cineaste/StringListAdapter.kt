package ba.unsa.etf.rma.cineaste

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StringListAdapter(
    var list: List<String>
) :RecyclerView.Adapter<StringListAdapter.SimpleViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimpleViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_simple_string, parent, false)

        return SimpleViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(
        holder: SimpleViewHolder,
        position: Int
    ) {
        holder.string.text = list[position]
    }

    inner class SimpleViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val string: TextView = itemView.findViewById(R.id.simple_string)
    }
}