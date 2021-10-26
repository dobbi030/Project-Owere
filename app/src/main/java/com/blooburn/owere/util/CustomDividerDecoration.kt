package com.blooburn.owere.util

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView

class CustomDividerDecoration(
    private val height: Float,
    @ColorInt color: Int
) : RecyclerView.ItemDecoration() {
    private val paint = Paint()

    init {
        paint.color = color
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (i in 0 until parent.childCount){
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin.toFloat()
            val bottom = top + height

            c.drawRect(child.left.toFloat(), top, child.right.toFloat(), bottom, paint)
        }
    }
}