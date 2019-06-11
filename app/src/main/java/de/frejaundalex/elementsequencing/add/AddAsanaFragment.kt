package de.frejaundalex.elementsequencing.add

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.frejaundalex.elementsequencing.R
import de.frejaundalex.elementsequencing.common.ListAdapter
import de.frejaundalex.elementsequencing.common.ListViewHolder
import de.frejaundalex.elementsequencing.db.model.AsanaImageCategory
import de.frejaundalex.elementsequencing.view.DifficultyBar
import de.frejaundalex.elementsequencing.view.ElementSelection
import java.io.File
import java.io.IOException
import java.io.Serializable

data class AsanaImagePair(val category: AsanaImageCategory, val uri: Uri) : Serializable

class AddAsanaFragment : Fragment() {

    private lateinit var viewModel: AddAsanaFragmentViewModel
    private val storageDir: File get() = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

    private lateinit var nameInput: EditText
    private lateinit var sanskritInput: EditText
    private lateinit var elementSelection: ElementSelection
    private lateinit var difficultyBar: DifficultyBar
    private lateinit var notesInput: EditText

    private lateinit var saveButton: Button
    private lateinit var abortButton: Button
    private lateinit var backArrow: View

    private val imageListAdapter =
        ListAdapter(R.layout.create_image_button) { view -> ImageItemViewHolder(view, ::dispatchTakePictureIntent) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddAsanaFragmentViewModel::class.java)
        viewModel.asanaImages.observe(this, Observer {
            imageListAdapter.items = it
            imageListAdapter.notifyDataSetChanged()
        })
        viewModel.closeActivity.observe(this, Observer {
            activity!!.finish()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_asana_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameInput = view.findViewById(R.id.AddAsanaFragment_nameInput)
        sanskritInput = view.findViewById(R.id.AddAsanaFragment_sanskritInput)
        notesInput = view.findViewById(R.id.AddAsanaFragment_notesInput)
        elementSelection = view.findViewById(R.id.AddAsanaFragment_element_selection)
        difficultyBar = view.findViewById(R.id.AddAsanaFragment_difficulty)

        saveButton = view.findViewById(R.id.AddAsanaFragment_saveButton)
        abortButton = view.findViewById(R.id.AddAsanaFragment_abortButton)
        backArrow = view.findViewById(R.id.AddAsanaFragment_backArrow)

        val imageRecyclerView = view.findViewById<RecyclerView>(R.id.AddAsanaFragment_imageRecycler)
        imageRecyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)

        imageRecyclerView.adapter = imageListAdapter

        abortButton.setOnClickListener { activity?.finish() }
        backArrow.setOnClickListener { activity?.finish() }
        saveButton.setOnClickListener(this::onSave)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                AsanaImageCategory.Photo.code -> setNewImages(AsanaImageCategory.Photo)
                AsanaImageCategory.Stickfigure.code -> setNewImages(AsanaImageCategory.Stickfigure)
            }
        }
    }

    private fun onSave(view: View) {
        viewModel.onSave(
            nameInput.text.toString(),
            sanskritInput.text.toString(),
            notesInput.text.toString(),
            elementSelection.selectedElements,
            difficultyBar.difficulty
        )
    }

    private fun setNewImages(category: AsanaImageCategory) {
        viewModel.onImageCaptured(category)
    }

    private fun dispatchTakePictureIntent(item: AsanaImagePair) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    viewModel.createImageFile(storageDir)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context!!,
                        "de.frejaundalex.elementsequencing.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, item.category.code)
                }
            }
        }
    }

}

class ImageItemViewHolder(view: View, private val onClick: (AsanaImagePair) -> Unit) :
    ListViewHolder<AsanaImagePair>(view) {
    val imageView: ImageView = view.findViewById(R.id.imagePreview)
    val categoryText: TextView = view.findViewById(R.id.CreateImageButton_category)

    override fun bind(item: AsanaImagePair) {
        categoryText.text = item.category.name
        imageView.setOnClickListener { onClick(item) }
        Picasso.get().load(item.uri).fit().centerCrop().into(imageView)
    }
}


fun EditText.getTextAsInt(): Int {
    return if (text.toString().isBlank()) 0
    else text.toString().toInt()
}