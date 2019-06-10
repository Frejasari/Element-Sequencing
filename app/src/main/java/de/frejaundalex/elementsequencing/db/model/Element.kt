package de.frejaundalex.elementsequencing.db.model

import androidx.annotation.ColorRes
import com.google.gson.Gson
import de.frejaundalex.elementsequencing.R.color.*
import io.objectbox.converter.PropertyConverter

sealed class Element(val name: String, @ColorRes val color: Int) {
    object Earth : Element("Earth", earth)
    object Water : Element("Water", water)
    object Fire : Element("Fire", fire)
    object Air : Element("Air", air)
    object Space : Element("Space", space)

    companion object {
        val all = listOf(Earth, Water, Fire, Air, Space)
    }
}

class ElementConverter : PropertyConverter<Element, String> {
    override fun convertToDatabaseValue(entityProperty: Element?): String? {
        if (entityProperty == null) return null
        return entityProperty.name
    }

    override fun convertToEntityProperty(databaseValue: String?): Element? {
        if (databaseValue == null) return null
        return Element.all.first { it.name == databaseValue }
    }
}

class ElementListConverter : PropertyConverter<List<Element>, String> {
    private val gson = Gson()
    private val elementConverter = ElementConverter()

    override fun convertToDatabaseValue(entityProperty: List<Element>?): String? {
        if (entityProperty == null) return null
        return gson.toJson(entityProperty.map { elementConverter.convertToDatabaseValue(it) })
    }

    override fun convertToEntityProperty(databaseValue: String?): List<Element>? {
        if (databaseValue == null) return null
        return gson.fromJson(databaseValue, List::class.java)
            .mapNotNull { elementConverter.convertToEntityProperty(it as String?) }
    }
}
