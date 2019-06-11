package de.frejaundalex.elementsequencing.add

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.frejaundalex.elementsequencing.db.ObjectBox
import de.frejaundalex.elementsequencing.db.model.*
import de.frejaundalex.elementsequencing.error.ValidationError
import io.objectbox.kotlin.boxFor
import org.threeten.bp.LocalDateTime
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddAsanaFragmentViewModel : ViewModel() {
    private val personalAsanaBox = ObjectBox.get().boxFor(PersonalAsana::class)

    val asanaImages = MutableLiveData<List<AsanaImagePair>>().apply {
        value = listOf(
            AsanaImagePair(AsanaImageCategory.Photo, Uri.EMPTY),
            AsanaImagePair(AsanaImageCategory.Stickfigure, Uri.EMPTY)
        )
    }
    val closeActivity = MutableLiveData<Unit>()

    private var currentPhotoPath: String? = null

    fun onImageCaptured(category: AsanaImageCategory) {
        val file = File(currentPhotoPath)
        val uri = Uri.fromFile(file)
        asanaImages.value = asanaImages.value!!.map { item ->
            if (item.category == category) {
                item.copy(uri = uri)
            } else item
        }
    }

    @Throws(IOException::class)
    fun createImageFile(storageDir: File): File {
        // Create an imageView file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


    fun onSave(name: String, sanskritName: String, notes: String, elements: List<Element>, difficulty: Difficulty) {
        val errors = this.validateInput(name)
        if (errors.isEmpty()) {
            val asana = Asana(
                0, // new Object, initialized with 0
                createdAt = LocalDateTime.now().toString(),
                name = name,
                sanskritName = sanskritName,
                images = asanaImages.value!!.map { Pair(it.category, it.uri) }.toMap()
//                    listOf(notesInput.text.toString())
            )
            val personalAsana = PersonalAsana(
                0,
                createdAt = LocalDateTime.now().toString(),
                notes = notes,
                elements = elements,
                level = difficulty,
                category = null
            )
            personalAsana.asana.target = asana
            personalAsanaBox.put(
                personalAsana
            )
            closeActivity.value = Unit
        } else {
//            errors.forEach { view!!.findViewById<TextInputLayout>(it.viewId).error = it.message }
        }
    }

    private fun validateInput(name: String): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()
        if (name.isBlank()) {
        }
        return errors
    }
}