package com.dk.doodlekong.ui.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import com.dk.doodlekong.data.remote.ws.models.DrawData
import com.dk.doodlekong.util.Constants
import java.util.Stack
import kotlin.math.abs

class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
): View(context, attrs) {

    private var viewWidth: Int? = null
    private var viewHeight: Int? = null
    private var bmp: Bitmap? = null
    private var canvas: Canvas? = null
    private var curX: Float? = null
    private var curY: Float? = null
    var smoothness = 5
    var isDrawing = false

    private var paint = Paint(Paint.DITHER_FLAG).apply {
        isDither = true
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        strokeWidth = Constants.DEFAULT_PAINT_THICKNESS
    }

    private var path = Path()
    private var paths = Stack<PathData>()
    private var pathDataChangedListener: ((Stack<PathData>) -> Unit)? = null

    var roomName: String? = null
    var isUserDrawing = false
        set(value) {
            isEnabled = value
            field = value
        }

    private var onDrawListener: ((DrawData) -> Unit)? = null

    fun setOnDrawListener(listener: ((DrawData) -> Unit)) {
        onDrawListener = listener
    }

    fun setPathDataChangedListener(listener: (Stack<PathData>) -> Unit) {
        pathDataChangedListener = listener
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int){
        viewWidth = w
        viewHeight = h
        bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bmp!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val initialColour = paint.color
        val initialThickness = paint.strokeWidth

        for (pathData in paths) {
            paint.apply {
                color = pathData.colour
                strokeWidth = pathData.thickness
            }
            canvas.drawPath(pathData.path, paint)
        }
        paint.apply {
            color = initialColour
            strokeWidth = initialThickness
        }
        canvas.drawPath(path, paint)
    }

    private fun startedTouch(x: Float, y: Float) {
        path.reset()
        path.moveTo(x, y)
        curX = x
        curY = y
        onDrawListener?.let { draw ->
            val drawData = createDrawData(x, y, x, y, ACTION_DOWN)
            draw(drawData)
        }
        invalidate()
    }

    private fun moveTouch(toX: Float, toY: Float) {
        val dx = abs(toX - (curX ?: return))
        val dy = abs(toY - (curY ?: return))

        if(dx >= smoothness || dy >= smoothness) {
            isDrawing = true
            path.quadTo(curX!!, curY!!, (curX!! + toX) / 2f, (curY!! + toY) / 2f)

            onDrawListener?.let { draw ->
                val drawData = createDrawData(curX!!, curY!!, toX, toY, ACTION_MOVE)
                draw(drawData)
            }

            curX = toX
            curY = toY
            invalidate()
        }
    }

    private fun releaseTouch() {
        isDrawing = false
        path.lineTo(curX ?: return, curY ?: return)
        paths.push(PathData(path, paint.color, paint.strokeWidth))
        pathDataChangedListener?.let { change ->
            change(paths)
        }
        onDrawListener?.let { draw ->
            val drawData = createDrawData(curX!!, curY!!, curX!!, curY!!, ACTION_UP)
            draw(drawData)
        }
        path = Path()
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(!isEnabled) {
            return false
        }
        val newX = event?.x
        val newY = event?.y
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> startedTouch(newX ?: return false, newY ?: return false)
            MotionEvent.ACTION_MOVE -> moveTouch(newX ?: return false, newY ?: return false)
            MotionEvent.ACTION_UP -> releaseTouch()
        }
        return true
    }

    private fun createDrawData(
        fromX: Float,
        fromY: Float,
        toX: Float,
        toY: Float,
        motionEvent: Int
    ): DrawData {
        return DrawData(
            roomName ?: throw IllegalStateException("Must state the roomName in drawing view"),
            paint.color,
            paint.strokeWidth,
            fromX / viewWidth!!,
            fromY / viewHeight!!,
            toX / viewWidth!!,
            toY / viewHeight!!,
            motionEvent
        )
    }

    fun setThickness(thickness: Float) {
        paint.strokeWidth = thickness
    }

    fun setColour(colour: Int) {
        paint.color = colour
    }

    fun clear() {
        canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY)
        paths.clear()
    }


    data class PathData(val path: Path, val colour: Int, val thickness: Float)
}