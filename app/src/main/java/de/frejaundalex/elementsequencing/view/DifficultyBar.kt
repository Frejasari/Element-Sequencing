package de.frejaundalex.elementsequencing.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import de.frejaundalex.elementsequencing.R
import de.frejaundalex.elementsequencing.db.model.Difficulty
import kotlin.properties.Delegates

class DifficultyBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    private var difficultyBar: SeekBar
    private lateinit var label: TextView
    var difficulty: Difficulty by Delegates.observable(Difficulty.Beginner)
    { _, _: Difficulty, newValue: Difficulty -> label.text = newValue.text }
        private set

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.difficulty_bar, this, true)

        difficultyBar = view.findViewById(R.id.DifficultyBar_progress_bar)
        label = view.findViewById(R.id.DifficultyBar_selected_text)

        difficultyBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when (progress) {
                    0 -> difficulty = Difficulty.Beginner
                    1 -> difficulty = Difficulty.Intermediate
                    2 -> difficulty = Difficulty.Advanced
                    else -> throw IllegalArgumentException("difficulty out of range")
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        orientation = VERTICAL
        difficultyBar.progress = 0
        difficulty = Difficulty.Beginner
        difficultyBar.max = 2
    }
}
