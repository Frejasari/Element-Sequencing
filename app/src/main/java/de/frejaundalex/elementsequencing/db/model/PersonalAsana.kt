package de.frejaundalex.elementsequencing.db.model

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class PersonalAsana(
    @Id var id: Long,
    val createdAt: String,
    val notes: String?,

    @Convert(converter = ElementListConverter::class, dbType = String::class)
    val elements: List<Element>,

    @Convert(converter = DifficultyConverter::class, dbType = Int::class)
    val level: Difficulty,
    val category: String?
) {
    lateinit var asana: ToOne<Asana>
}
