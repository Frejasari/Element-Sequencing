package de.frejaundalex.elementsequencing.add

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import de.frejaundalex.elementsequencing.R

class AddDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_dialog_item, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
//            .setView(R.layout.fragment_add_dialog)
            .setAdapter(
                AddListAdapter(
                    context!!,
                    listOf(
                        AddItem("Asana", "Hier gibts ne Asana, also eine Koerperuebung"),
                        AddItem(
                            "Stunde",
                            "Hier kannst du eine ganze Stunde erstellen. Da kannst DU Text mit deinen Asanas verbinden und dir aussserdem jede Menge Notizen machen."
                        )
                    )
                )
            ) { dialog, position ->
                when (position) {
                    0 -> {
                        context!!.startActivity(Intent(context!!, AddActivity::class.java))
                    }
                }
            }
            .setTitle("Was moechtest du erstellen?")
            .create()
    }

}


data class AddItem(val title: String, val description: String)

class AddListAdapter(context: Context, items: List<AddItem>) :
    ArrayAdapter<AddItem>(context, R.layout.add_dialog_item, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var cView = convertView
        if (convertView == null) {
            cView = LayoutInflater.from(context).inflate(R.layout.add_dialog_item, parent, false)
        }
        cView as View
        val title = cView.findViewById<TextView>(R.id.add_dialog_item_title)
        val description = cView.findViewById<TextView>(R.id.add_dialog_item_description)
        val item = getItem(position)
        title.text = item.title
        description.text = item.description
        return cView
    }
}

