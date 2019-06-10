package de.frejaundalex.elementsequencing.add

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.frejaundalex.elementsequencing.db.model.AsanaImageCategory
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddAsanaFragmentViewModel : ViewModel() {

    val asanaImages = MutableLiveData<List<AsanaImagePair>>().apply {
        value = listOf(
            AsanaImagePair(AsanaImageCategory.Photo, Uri.EMPTY),
            AsanaImagePair(AsanaImageCategory.Stickfigure, Uri.EMPTY)
        )
    }

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
}