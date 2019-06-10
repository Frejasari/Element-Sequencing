package de.frejaundalex.elementsequencing.db.model

import io.objectbox.converter.PropertyConverter


sealed class Difficulty(var lvl: Int, var text: String) {
    object Beginner : Difficulty(0, "Anfaenger")
    object Intermediate : Difficulty(2, "Fortgeschritten")
    object Advanced : Difficulty(4, "Profi")

    companion object {
        val all = listOf(Beginner, Intermediate, Advanced)
    }
}

class DifficultyConverter : PropertyConverter<Difficulty, Int> {
    override fun convertToDatabaseValue(entityProperty: Difficulty?): Int? {
        if (entityProperty == null) return null
        return entityProperty.lvl
    }

    override fun convertToEntityProperty(databaseValue: Int?): Difficulty? {
        if (databaseValue == null) {
            return null
        }
        return Difficulty.all.first { it.lvl == databaseValue }
    }

}