package com.blackflagbin.kcommon.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.TextView

class DrawableCenterTextView(context:Context,attrs:AttributeSet? = null):TextView(context,attrs) {
    override fun onDraw(canvas: Canvas?) {
        val drawables = compoundDrawables
        if (drawables != null) {
            val drawableLeft = drawables[0]
            if (drawableLeft != null) {
                val textWidth = paint.measureText(text.toString())
                val drawablePadding = compoundDrawablePadding
                var drawableWidth = 0
                drawableWidth = drawableLeft.intrinsicWidth
                val bodyWidth = textWidth + drawableWidth.toFloat() + drawablePadding.toFloat()
                canvas?.save()
                canvas?.translate((width - bodyWidth) / 2, 0f)
            }
        }
        super.onDraw(canvas)
    }
}