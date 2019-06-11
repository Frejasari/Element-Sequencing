package de.frejaundalex.elementsequencing.db.model

import android.net.Uri
import com.google.gson.Gson
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter

@Entity
data class Asana(
    @Id var id: Long,
    val createdAt: String,
    val name: String,
    val sanskritName: String,

    @Convert(converter = ImageMapConverter::class, dbType = String::class)
    val images: Map<AsanaImageCategory, Uri>
)

class ImageMapConverter : PropertyConverter<Map<AsanaImageCategory, Uri>, String> {
    private val gson = Gson()
    override fun convertToDatabaseValue(entityProperty: Map<AsanaImageCategory, Uri>): String {
        val stringMap = entityProperty.map { Pair(it.key, it.value.toString()) }.toMap()
        return gson.toJson(stringMap)
    }

    override fun convertToEntityProperty(databaseValue: String): Map<AsanaImageCategory, Uri> {
        return gson.fromJson<Map<AsanaImageCategory, Uri>>(databaseValue, HashMap::class.java)
    }
}