package de.frejaundalex.elementsequencing.view

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import de.frejaundalex.elementsequencing.R
import de.frejaundalex.elementsequencing.db.model.Element

class ElementSelection @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var earth: ImageView
    private var water: ImageView
    private var fire: ImageView
    private var air: ImageView
    private var space: ImageView

    var selectedElements: List<Element> = emptyList()
        private set

    init {
        orientation = HORIZONTAL

        val view = LayoutInflater.from(context).inflate(R.layout.element_selection, this, true)
        earth = view.findViewById(R.id.earth)
        water = view.findViewById(R.id.water)
        fire = view.findViewById(R.id.fire)
        air = view.findViewById(R.id.air)
        space = view.findViewById(R.id.space)

        earth.setOnClickListener { handleElementClick(Element.Earth, it as ImageView) }
        water.setOnClickListener { handleElementClick(Element.Water, it as ImageView) }
        fire.setOnClickListener { handleElementClick(Element.Fire, it as ImageView) }
        air.setOnClickListener { handleElementClick(Element.Air, it as ImageView) }
        space.setOnClickListener { handleElementClick(Element.Space, it as ImageView) }
    }

    private fun handleElementClick(element: Element, button: ImageView) {
        if (isElementSelected(element)) {
            selectedElements = selectedElements.filter { it != element }
        } else {
            selectedElements = selectedElements + element
        }
        setElementColor(element, button)
    }

    private fun setElementColor(element: Element, button: ImageView) {
        if (isElementSelected(element)) {
            button.setColorFilter(
                ContextCompat.getColor(button.context, element.color),
                PorterDuff.Mode.SRC_ATOP
            )
        } else {
            button.setColorFilter(
                Color.TRANSPARENT,
                PorterDuff.Mode.SRC_ATOP
            )
        }
    }

    private fun isElementSelected(element: Element): Boolean {
        return selectedElements.contains(element)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        state as Bundle
        super.onRestoreInstanceState(state.getParcelable("superState"))
        selectedElements =
            state.getStringArrayList("elements")!!.map { name -> Element.all.first { element -> element.name == name } }

        setElementColor(Element.Earth, earth)
        setElementColor(Element.Water, water)
        setElementColor(Element.Fire, fire)
        setElementColor(Element.Air, air)
        setElementColor(Element.Space, space)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return Bundle().apply {
            putParcelable("superState", superState)
            putStringArrayList("elements", ArrayList(selectedElements.map { it.name }))
        }

    }
}