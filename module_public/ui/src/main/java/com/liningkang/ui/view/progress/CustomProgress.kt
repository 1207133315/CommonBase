package com.liningkang.ui.view.progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.liningkang.common.R
import com.liningkang.utils.DeviceUtils

class CustomProgress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val _paint: Paint
    private val _rectF: RectF
    private val _rect: Rect
    var current = 0
        set(value) {
            field=value
            if (value<=max){
                invalidate()
            }
        }

     var max = 100
         set(value) {
             field=value

         }
     var acrColor= Color.parseColor("#7B58FF")
         set(value) {
             field=value

         }
     var acrBackgroundColor=  Color.parseColor("#147B58FF")
         set(value) {
             field=value

         }

    //圆弧（也可以说是圆环）的宽度
     var arcWidth = 30f
        set(value) {
            field=value

        }

    //控件的宽度
    private var _width = 0f
    private var textSize=context.resources.getDimensionPixelSize(R.dimen.sp_34).toFloat()


     var textColor=Color.parseColor("#7B58FF")
        set(value) {
            field=value
            invalidate()
        }

    init {
        _paint = Paint()
        _paint.isAntiAlias = true
        _rectF = RectF()
        _rect = Rect()
        val typedArray = context.obtainStyledAttributes(attrs,com.liningkang.ui.R.styleable.CustomProgress)
        arcWidth=  typedArray.getDimension(com.liningkang.ui.R.styleable.CustomProgress_arcWidth,
            arcWidth
        )/2
        textSize =  DeviceUtils.dp2px(context,typedArray.getDimension(com.liningkang.ui.R.styleable.CustomProgress_acrTextSize,
            textSize
        ))
        acrBackgroundColor= typedArray.getColor(com.liningkang.ui.R.styleable.CustomProgress_acrBackgroundColor,acrBackgroundColor)
        acrColor= typedArray.getColor(com.liningkang.ui.R.styleable.CustomProgress_acrColor,acrColor)
        textColor= typedArray.getColor(com.liningkang.ui.R.styleable.CustomProgress_acrTextColor,textColor)
        max= typedArray.getInt(com.liningkang.ui.R.styleable.CustomProgress_acrMax,max)
        typedArray.recycle()


    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //getMeasuredWidth获取的是view的原始大小，也就是xml中配置或者代码中设置的大小
        //getWidth获取的是view最终显示的大小，这个大小不一定等于原始大小
        _width = measuredWidth.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制圆形
        //设置为空心圆，如果不理解绘制弧线是什么意思就把这里的属性改为“填充”，跑一下瞬间就明白了
        _paint.style = Paint.Style.STROKE
        //设置圆弧的宽度（圆环的宽度）
        _paint.strokeWidth = arcWidth
        _paint.color =acrBackgroundColor
        //大圆的半径
        val bigCircleRadius = _width / 2
        //小圆的半径
        val smallCircleRadius = bigCircleRadius - arcWidth
        //绘制小圆
        canvas.drawCircle(bigCircleRadius, bigCircleRadius, smallCircleRadius, _paint)
        _paint.color = acrColor
        _paint.strokeCap = Paint.Cap.ROUND
        _rectF[arcWidth, arcWidth, _width - arcWidth] = _width - arcWidth
        //绘制圆弧
        canvas.drawArc(_rectF, 270f, (current * 360 / max).toFloat(), false, _paint)
        //计算百分比
        val txt = (current * 100 / max).toString() + "%"
        _paint.strokeWidth = 0f
        _paint.textSize =textSize

        _paint.style = Paint.Style.FILL
        _paint.getTextBounds(txt, 0, txt.length, _rect)
        _paint.color = textColor
        _paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        //绘制百分比
        canvas.drawText(
            txt,
            bigCircleRadius - _rect.width() / 2,
            bigCircleRadius + _rect.height() / 2,
            _paint
        )
    }

    companion object {
        private const val TAG = "MyCircleProgress"
    }
}