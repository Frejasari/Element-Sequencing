package de.frejaundalex.elementsequencing.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListAdapter<T>(
    private val layout: Int,
    private val createViewHolder: (View) -> ListViewHolder<T>
) :
    RecyclerView.Adapter<ListViewHolder<T>>() {

    var items: List<T> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder<T> {
        return createViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ListViewHolder<T>, position: Int) {
        holder.bind(items[position])
    }
}

abstract class ListViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}