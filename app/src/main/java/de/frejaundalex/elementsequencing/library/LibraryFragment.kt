package de.frejaundalex.elementsequencing.library

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.frejaundalex.elementsequencing.R
import de.frejaundalex.elementsequencing.db.ObjectBox
import de.frejaundalex.elementsequencing.db.model.Asana
import de.frejaundalex.elementsequencing.db.model.AsanaImageCategory
import de.frejaundalex.elementsequencing.db.model.PersonalAsana
import de.frejaundalex.elementsequencing.view.ElementView
import io.objectbox.android.AndroidScheduler
import io.objectbox.kotlin.boxFor
import io.objectbox.reactive.DataSubscriptionList


class LibraryFragment : Fragment() {

    private lateinit var viewModel: LibraryViewModel

    private val dataSubscriptionList = DataSubscriptionList()

    private val personalAsanaBox = ObjectBox.get().boxFor(PersonalAsana::class)
    private val asanaBox = ObjectBox.get().boxFor(Asana::class)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.library_fragment, container, false)

        val bookRecyclerView = view.findViewById<RecyclerView>(R.id.asanaRecyclerView)
        bookRecyclerView.layoutManager = GridLayoutManager(view.context, 3)
        val bookListAdapter = AsanaListAdapter(this::onAsanaClicked)
        bookRecyclerView.adapter = bookListAdapter
        val query = personalAsanaBox.query().build()
        val queryAsana = asanaBox.query().build()

        Log.i("ObjectBox", " Items in asana box ${queryAsana.find()}")
        Log.i("ObjectBox", " Items in personal box ${query.find()}")
        query.subscribe(dataSubscriptionList)
            .on(AndroidScheduler.mainThread())
            .onError { error ->
                Log.e("ObjectBoxError", "Error: ${error.localizedMessage}")
                error.printStackTrace()
            }
            .observer { data ->
                bookListAdapter.personalAsanas = data.reversed()
                bookListAdapter.notifyDataSetChanged()
            }

        return view
    }

    private fun onAsanaClicked(id: Long) {
//        startActivity(Intent(this.context, EditBookActivity::class.java).apply {
//            putExtra(BOOK_ID, visibility)
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

class AsanaListAdapter(val onAsanaClicked: (id: Long) -> Unit) : RecyclerView.Adapter<AsanaViewHolder>() {

    var personalAsanas: List<PersonalAsana> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsanaViewHolder {
        return AsanaViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.asana_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return personalAsanas.size
    }

    override fun onBindViewHolder(holder: AsanaViewHolder, position: Int) {
        val personalAsana = personalAsanas[position]
        var uri: Uri? = null
        val asana = personalAsana.asana.target
        val stickfigureImg = asana.images[AsanaImageCategory.Stickfigure]
        val photoImg = asana.images[AsanaImageCategory.Photo]

        if (stickfigureImg != Uri.EMPTY && stickfigureImg != null) {
            uri = stickfigureImg
        } else if (photoImg != null && photoImg != Uri.EMPTY) {
            uri = photoImg
        }
        if (uri != null) {
            Picasso.get().load(uri).fit().centerCrop().into(holder.image)
        }

        holder.elementView.setElements(*personalAsana.elements.toTypedArray())

        holder.name.text = asana.name

        holder.asanaView.setOnClickListener { onAsanaClicked(personalAsana.id) }
    }
}

class AsanaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val image: ImageView = view.findViewById(R.id.LibraryAsanaItem_image)
    val asanaView: ConstraintLayout = view.findViewById(R.id.LibraryAsanaItem_layout)
    val name: TextView = view.findViewById(R.id.LibraryAsanaItem_name)
    val elementView: ElementView = view.findViewById(R.id.LibraryAsanaItem_elementView)
}
