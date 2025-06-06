package com.dk.doodlekong.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.dk.doodlekong.R
import kotlin.math.min
import kotlin.properties.Delegates

class ColourRadioButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
): AppCompatRadioButton(context, attrs) {

    private var buttonColour by Delegates.notNull<Int>()
    private var radius = 25f

    private var viewWidth by Delegates.notNull<Int>()
    private var viewHeight by Delegates.notNull<Int>()

    private val buttonPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val selectionPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ColourRadioButton, 0, 0).apply {
            try {
                buttonColour = getColor(R.styleable.ColourRadioButton_buttonColour, Color.BLACK)
            } finally {
                recycle()
            }
            buttonPaint.apply {
                color = buttonColour
                style = Paint.Style.FILL
            }
            selectionPaint.apply {
                color = Color.BLACK
                style = Paint.Style.STROKE
                strokeWidth = 12f
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h

        radius = min(w, h) / 2 * 0.8f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(viewWidth / 2f, viewHeight / 2f, radius, buttonPaint)
        if(isChecked) {
            canvas.drawCircle(viewWidth / 2f, viewHeight / 2f, radius * 1.1f, selectionPaint)
        }
    }
}