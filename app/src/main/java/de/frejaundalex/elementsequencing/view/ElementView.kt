package de.frejaundalex.elementsequencing.view

import android.content.Context
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
import de.frejaundalex.elementsequencing.R
import de.frejaundalex.elementsequencing.db.model.Element
import kotlin.properties.Delegates


class ElementView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    enum class UnselectedType(val id: Int, val visibility: Int, val withColor: Boolean) {
        INVISIBLE_COLORED(0, View.INVISIBLE, true),
        INVISIBLE(1, View.INVISIBLE, false),
        GONE_COLORED(2, View.GONE, true),
        GONE(3, View.GONE, false),
        GREY(4, View.VISIBLE, true)
    }


    private lateinit var earth: ImageView
    private lateinit var earthSpacer: Space
    private lateinit var water: ImageView
    private lateinit var waterSpacer: Space

    private lateinit var fire: ImageView
    private lateinit var fireSpacer: Space

    private lateinit var air: ImageView
    private lateinit var airSpacer: Space

    private lateinit var space: ImageView

    private val elementViewMap: Map<Element, ImageView> by lazy {
        mapOf(
            Element.Earth to earth,
            Element.Water to water,
            Element.Fire to fire,
            Element.Air to air,
            Element.Space to space
        )
    }


    var unselectedType: UnselectedType by Delegates.observable(UnselectedType.GREY) { property, oldValue, newValue ->
        setup(selectedElements, newValue)
    }

    private var selectedElements: Map<Element, Boolean> by Delegates.observable(
        mapOf(
            Element.Earth to true, Element.Water to true,
            Element.Fire to true, Element.Air to true, Element.Space to true
        )
    ) { property, oldValue, newValue -> setup(newValue, unselectedType) }

    private fun setup(elements: Map<Element, Boolean>, unselectedType: UnselectedType) {
        elements.entries.forEach { (element, visibility) ->
            if (visibility) {
                when (element) {
                    Element.Earth -> {
                        earth.visibility = View.VISIBLE
                        earthSpacer.visibility = View.VISIBLE
                    }
                    Element.Water -> {
                        water.visibility = View.VISIBLE
                        waterSpacer.visibility = View.VISIBLE
                    }
                    Element.Fire -> {
                        fire.visibility = View.VISIBLE
                        fireSpacer.visibility = View.VISIBLE
                    }
                    Element.Air -> {
                        air.visibility = View.VISIBLE
                        airSpacer.visibility = View.VISIBLE
                    }
                    Element.Space -> {
                        space.visibility = View.VISIBLE
                    }
                }
                setElementColor(element, unselectedType.withColor)
            } else {
                when (element) {
                    Element.Earth -> {
                        earth.visibility = unselectedType.visibility
                        earthSpacer.visibility = unselectedType.visibility
                    }
                    Element.Water -> {
                        water.visibility = unselectedType.visibility
                        waterSpacer.visibility = unselectedType.visibility
                    }
                    Element.Fire -> {
                        fire.visibility = unselectedType.visibility
                        fireSpacer.visibility = unselectedType.visibility
                    }
                    Element.Air -> {
                        air.visibility = unselectedType.visibility
                        airSpacer.visibility = unselectedType.visibility
                    }
                    Element.Space -> {
                        space.visibility = unselectedType.visibility
                    }
                }
                setElementColor(element, false)
            }
        }
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
        elementViewMap.keys.forEach { element -> setElementColor(element, true) }
    }

    private fun setElementColor(element: Element, withColor: Boolean) {
        val elementView = elementViewMap[element]!!
        if (withColor) {
            elementView.setColorFilter(
                ContextCompat.getColor(elementView.context, element.color),
                PorterDuff.Mode.SRC_ATOP
            )
        } else {
            elementView.setColorFilter(
                ContextCompat.getColor(elementView.context, R.color.elementNotSelected),
                PorterDuff.Mode.SRC_ATOP
            )
        }
    }

    private fun setElement(isVisible: Boolean, element: Element) {
        val mutableMap = selectedElements.toMutableMap()
        mutableMap[element] = isVisible
        selectedElements = mutableMap
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        state as Bundle
        super.onRestoreInstanceState(state.getParcelable("superState"))
        selectedElements.entries.forEach { (element, _) ->
            setElement(state.getBoolean(element.name), element)
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
            bundle.putBoolean(element.name, visibility)
        }
        return bundle
    }
}