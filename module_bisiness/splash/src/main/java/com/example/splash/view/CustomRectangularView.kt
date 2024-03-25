package com.example.splash.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener

class CustomRectangularView @JvmOverloads constructor(context: Context, attrs: AttributeSet) :
    View(context, attrs) {
    private val blockColors = arrayOf(Color.RED, Color.GREEN, Color.BLUE)
    var blockRatios = floatArrayOf(0.2f, 0.4f, 0.4f)
    private val animationDuration = 2000L // 动画持续时间（毫秒）
    private val maskPaint = Paint()
    private val paint = Paint()

    private val animatedRatios = FloatArray(blockColors.size)
    private val animatorList = mutableListOf<ValueAnimator>()
    private var blockIndex = 0


    init {
        if (blockColors.size != blockRatios.size) {
            throw IllegalArgumentException("blockColors and blockRatios must have the same size")
        }

    }

    private val cornerRadius = 20f // 圆角半径


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        val totalWidth = width.toFloat()
        val totalHeight = height.toFloat()
        var currentX = 0f
        for (i in blockColors.indices) {
            val blockWidth = totalWidth * blockRatios[i]
            val drawWidth = animatedRatios[i] * blockWidth
            paint.color = blockColors[i]
            val rect = RectF(currentX, 0f, currentX + drawWidth, totalHeight)
            // 绘制整体的四个角圆角
            var radii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
            if (i==0){
                radii=floatArrayOf(cornerRadius, cornerRadius, 0f, 0f, 0f, 0f, cornerRadius, cornerRadius)
            }else if (i==blockColors.size-1){
                radii=floatArrayOf(0f, 0f, cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0f, 0f)
            }

            // 创建一个路径，用于绘制色块，使其与背景形状相匹配
            val blockPath = Path()
            blockPath.addRoundRect(
                rect,
                radii,
                Path.Direction.CW
            )
            canvas.drawPath(blockPath, paint)
            currentX += blockWidth
        }

    }

    fun resetColorBlocks() {
        if (animatorList.isNotEmpty()) {
            for (i in blockColors.indices) {
                animatedRatios[i] = 0f
                animatorList[i].cancel()
            }
            blockIndex = 0
            invalidate()
        }
    }

    fun startColorAnimation() {
        if (blockIndex < 0 || blockIndex >= blockRatios.size) return
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = (animationDuration * blockRatios[blockIndex]).toLong()
        animator.interpolator = LinearInterpolator() // 设置为线性插值器
        animator.addUpdateListener { animation ->
            animatedRatios[blockIndex] = animation.animatedValue as Float
            invalidate()
        }
        animator.addListener(onEnd = {
            blockIndex++
            if (blockIndex < blockColors.size) {
                startColorAnimation()
            } else {
                blockIndex = 0
            }
        })
        animator.start()
        animatorList.add(animator)
    }
}