package de.frejaundalex.elementsequencing.db.model

import java.io.Serializable

sealed class AsanaImageCategory(val code: Int, val name: String) : Serializable {
    object Photo : AsanaImageCategory(19, "Foto")
    object Stickfigure : AsanaImageCategory(18, "Strichmännchen")

    companion object {
        val all = listOf(Photo, Stickfigure)

        fun fromCode(code: Int): AsanaImageCategory {
            return all.first { it.code == code }
        }
    }
}