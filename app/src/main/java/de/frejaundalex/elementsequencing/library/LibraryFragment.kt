package de.frejaundalex.elementsequencing.library

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.frejaundalex.elementsequencing.db.ObjectBox
import de.frejaundalex.elementsequencing.db.model.Asana
import io.objectbox.android.AndroidScheduler
import io.objectbox.kotlin.boxFor
import io.objectbox.reactive.DataSubscriptionList


const val BOOK_ID = "BOOK_ID"

class LibraryFragment : Fragment() {

    companion object {
        fun newInstance() = LibraryFragment()
    }

    private lateinit var viewModel: LibraryViewModel

    private val dataSubscriptionList = DataSubscriptionList()

    private val bookBox = ObjectBox.get().boxFor(Asana::class)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(de.frejaundalex.elementsequencing.R.layout.library_fragment, container, false)

        val bookRecyclerView = view.findViewById<RecyclerView>(de.frejaundalex.elementsequencing.R.id.asanaRecyclerView)
        bookRecyclerView.layoutManager = LinearLayoutManager(view.context)
        val bookListAdapter = BookListAdapter(this::onBookClicked)
        bookRecyclerView.adapter = bookListAdapter
        val query = bookBox.query().build()
        query.subscribe(dataSubscriptionList)
            .on(AndroidScheduler.mainThread())
            .observer { data ->
                bookListAdapter.books = data.reversed();
                bookListAdapter.notifyDataSetChanged()
            }

        return view
    }

    private fun onBookClicked(id: Long) {
//        startActivity(Intent(this.context, EditBookActivity::class.java).apply {
//            putExtra(BOOK_ID, id)
//        })
        Log.i("TAG<", "MAKE CLICK HANDLING")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LibraryViewModel::class.java)
    }

    override fun onDestroyView() {
        dataSubscriptionList.cancel()
        super.onDestroyView()
    }
}

class BookListAdapter(val onBookClick: (id: Long) -> Unit) : RecyclerView.Adapter<BookItemViewHolder>() {

    var books: List<Asana> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                de.frejaundalex.elementsequencing.R.layout.asana_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        val book = books[position]

        holder.bookView.setOnClickListener { onBookClick(book.id) }
        if (book.sanskritName.isEmpty()) {
        } else {
        }
    }
}

class BookItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val cover: ImageView = view.findViewById(de.frejaundalex.elementsequencing.R.id.LibraryListItem_bookCover)
    val bookView: ConstraintLayout = view.findViewById(de.frejaundalex.elementsequencing.R.id.book)

}
