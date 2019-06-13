package de.frejaundalex.elementsequencing.view

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import androidx.core.content.ContextCompat
import androidx.core.view.children
import de.frejaundalex.elementsequencing.R
import de.frejaundalex.elementsequencing.db.model.Element
import kotlin.properties.Delegates

class ElementView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var earth: ImageView
    private lateinit var earthSpacer: Space
    private lateinit var water: ImageView
    private lateinit var waterSpacer: Space

    private lateinit var fire: ImageView
    private lateinit var fireSpacer: Space

    private lateinit var air: ImageView
    private lateinit var airSpacer: Space

    private lateinit var space: ImageView

    private var selectedElements by Delegates.observable(
        mapOf(
            Element.Earth to View.GONE, Element.Water to View.GONE,
            Element.Fire to View.GONE, Element.Air to View.GONE, Element.Space to View.GONE
        )
    ) { property, oldValue, newValue ->
        newValue.entries.forEach { (element, visibility) ->
            when (element) {
                Element.Earth -> {
                    earth.visibility = visibility
                    earthSpacer.visibility = visibility
                }
                Element.Water -> {
                    water.visibility = visibility
                    waterSpacer.visibility = visibility
                }
                Element.Fire -> {
                    fire.visibility = visibility
                    fireSpacer.visibility = visibility
                }
                Element.Air -> {
                    air.visibility = visibility
                    airSpacer.visibility = visibility
                }
                Element.Space -> {
                    space.visibility = visibility
                }
            }
        }
        val last = this.children.lastOrNull { child -> child.visibility == View.VISIBLE }
        if (last is Space) last.visibility = View.GONE
    }

    var withColor by Delegates.observable(true) { _, _, _ ->
        applyColors()
    }

    fun showWater(show: Boolean) = setElement(show, Element.Water)
    fun showEarth(show: Boolean) = setElement(show, Element.Earth)
    fun showFire(show: Boolean) = setElement(show, Element.Fire)
    fun showAir(show: Boolean) = setElement(show, Element.Air)
    fun showSpace(show: Boolean) = setElement(show, Element.Space)

    fun setElements(vararg elements: Element) {
        Element.all.forEach { element -> setElement(false, element) }
        elements.forEach { element ->
            setElement(true, element)
        }
    }

    init {
        orientation = HORIZONTAL

        val view = LayoutInflater.from(context).inflate(R.layout.elements, this, true)
        earth = view.findViewById(R.id.element_earth)
        earthSpacer = view.findViewById(R.id.element_earth_space)
        water = view.findViewById(R.id.element_water)
        waterSpacer = view.findViewById(R.id.element_water_space)
        fire = view.findViewById(R.id.element_fire)
        fireSpacer = view.findViewById(R.id.element_fire_space)
        air = view.findViewById(R.id.element_air)
        airSpacer = view.findViewById(R.id.element_air_space)
        space = view.findViewById(R.id.element_space)

        withColor = true
        if (isInEditMode) {
            showEarth(true)
            showFire(true)
        }
    }

    private fun applyColors() {
        setElementColor(Element.Earth, earth)
        setElementColor(Element.Water, water)
        setElementColor(Element.Fire, fire)
        setElementColor(Element.Air, air)
        setElementColor(Element.Space, space)
    }

    private fun setElementColor(element: Element, button: ImageView) {
        if (withColor) {
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

    private fun setElement(show: Boolean, element: Element) {
        if (show) {
            setElement(View.VISIBLE, element)
        } else {
            setElement(View.GONE, element)
        }
    }

    private fun setElement(visibility: Int, element: Element) {
        val mutableMap = selectedElements.toMutableMap()
        mutableMap[element] = visibility
        selectedElements = mutableMap
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        state as Bundle
        super.onRestoreInstanceState(state.getParcelable("superState"))
        selectedElements.entries.forEach { (element, _) ->
            setElement(state.getInt(element.name), element)
        }
        this.withColor = state.getBoolean("withColor")
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()

        val bundle = Bundle().apply {
            putParcelable("superState", superState)
            putBoolean("withColor", withColor)
        }
        selectedElements.entries.forEach { (element, visibility) ->
            bundle.putInt(element.name, visibility)
        }
        return bundle
    }
}