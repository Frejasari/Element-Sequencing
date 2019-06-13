package de.frejaundalex.elementsequencing.db.model

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
class PersonalAsana(
    @Id var id: Long = 0,
    var createdAt: String = "",
    var notes: String? = null,

    @Convert(converter = ElementListConverter::class, dbType = String::class)
    var elements: List<Element> = listOf(),

    @Convert(converter = DifficultyConverter::class, dbType = Int::class)
    var level: Difficulty = Difficulty.Beginner,
    var category: String? = null
) {
    lateinit var asana: ToOne<Asana>
}
