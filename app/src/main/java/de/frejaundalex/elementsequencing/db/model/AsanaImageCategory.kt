package de.frejaundalex.elementsequencing.db.model

import java.io.Serializable

sealed class AsanaImageCategory(val code: Int) : Serializable {
    object Photo : AsanaImageCategory(19)
    object Stickfigure : AsanaImageCategory(18)
}