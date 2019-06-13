package de.frejaundalex.elementsequencing.db.model

import androidx.annotation.ColorRes
import com.google.gson.Gson
import de.frejaundalex.elementsequencing.R.color.*
import io.objectbox.converter.PropertyConverter

sealed class Element(val code: Int, val name: String, @ColorRes val color: Int) {
    object Earth : Element(32, "Earth", earth)
    object Water : Element(33, "Water", water)
    object Fire : Element(34, "Fire", fire)
    object Air : Element(35, "Air", air)
    object Space : Element(36, "Space", space)

    companion object {
        val all = listOf(Earth, Water, Fire, Air, Space)
    }
}

class ElementConverter : PropertyConverter<Element, Int> {
    override fun convertToDatabaseValue(entityProperty: Element?): Int? {
        if (entityProperty == null) return null
        return entityProperty.code
    }

    override fun convertToEntityProperty(databaseValue: Int?): Element? {
        if (databaseValue == null) return null
        return Element.all.first { it.code == databaseValue }
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
            .mapNotNull { elementConverter.convertToEntityProperty((it as Double?)?.toInt()) }
    }
}
