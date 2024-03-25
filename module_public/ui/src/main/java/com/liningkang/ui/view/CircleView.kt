package com.liningkang.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.databinding.BindingAdapter
import com.liningkang.ui.R

/**
 * @Author ；Ningkang.Li
 * @Time ：2023/5/26日 16点
 * @Description ；---
 */
class CircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {

        @JvmStatic
        @BindingAdapter("bg_color")
        fun setBgColor(view: CircleView, color: Int) {
            view.bgColor = color
            view.paint.color = color
            view.invalidate()
        }

    }

    val paint: Paint = Paint()
    private var bgColor: Int = Color.RED

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView)
        bgColor = typedArray.getInt(R.styleable.CircleView_bgColor, Color.RED)
        paint.color = bgColor
    }

    fun setBgColor( color: Int) {
        bgColor = color
        paint.color = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val radius = Math.min(width, height) / 2f
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }
}