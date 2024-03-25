package com.liningkang.ui.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.liningkang.base.BaseApplication.Companion.context
import com.liningkang.ui.R
import com.liningkang.utils.LogUtils
import com.liningkang.utils.ToastUtils
import java.text.DecimalFormat


open class BoldTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    AppCompatTextView(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    companion object {
        @JvmStatic
        @BindingAdapter("text_bold")
        fun setTextBold(textView: BoldTextView, isBold: Boolean) {
            textView.setBold(isBold)


            //textView.typeface = typeface
        }


        @JvmStatic
        @BindingAdapter("bg_color")
        fun backgroundColor(view: View, colorId: Int) {
            if (colorId != 0) {
                view.setBackgroundColor(view.context.resources.getColor(colorId))
            }
        }

    }

    private var onClickListener: OnClickListener? = null
    private var isBold: Boolean = false

    init {
        includeFontPadding=false
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BoldTextView)
        isBold = typedArray.getBoolean(R.styleable.BoldTextView_isBold, isBold)
        var typefacePath = typedArray.getString(R.styleable.BoldTextView_typefacePath)
        typedArray.recycle()
        if (!typefacePath.isNullOrEmpty()) {
            try {
                val typeface =
                    Typeface.createFromAsset(context.assets, typefacePath)
                if (typeface != null) {
                    setTypeface(typeface)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


        }else{
            setBold(isBold)
        }

        setOnTouch()

    }

    override fun setOnClickListener(l: OnClickListener?) {
        this.onClickListener = l
        super.setOnClickListener(onClickListener)

    }


    fun setBold(isBold: Boolean) {
        this.isBold = isBold
        if (isBold) {
            setTypeface(null, Typeface.BOLD)
        } else {
            setTypeface(null, Typeface.NORMAL)
        }
    }

    fun getBold(): Boolean {
        return isBold
    }


    private fun setOnTouch() {

        setOnTouchListener { v, event ->
            super.onTouchEvent(event)
            if (onClickListener != null) {
                if (event?.action == MotionEvent.ACTION_UP) {
                    v.animate().scaleX(1.0f).scaleY(1.0f).duration = 50
                } else if (event?.action == MotionEvent.ACTION_DOWN) {
                    v.animate().scaleX(0.9f).scaleY(0.9f).duration = 20
                }
                true
            } else {
                false
            }

        }

    }
}