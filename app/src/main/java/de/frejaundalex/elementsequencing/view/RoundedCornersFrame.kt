package de.frejaundalex.elementsequencing.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout


class RoundedCornersFrame @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        this.clipToOutline = true
        this.outlineProvider = ClipOutlineProvider()
    }
}