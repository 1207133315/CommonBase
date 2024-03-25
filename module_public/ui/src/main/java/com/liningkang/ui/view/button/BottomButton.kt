package com.liningkang.ui.view.button

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.liningkang.ui.R
import kotlinx.android.synthetic.main.bottom_button_layout.view.doneButton

class BottomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        val view = inflate(context, R.layout.bottom_button_layout, this)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomButton)
        var text = typedArray.getString(R.styleable.BottomButton_buttonText)?: resources.getString(R.string.next)
        typedArray.recycle()
        doneButton.text = text
        setOnTouch()
    }

    fun setButtonText(text:String){
        doneButton.text = text
    }
    private var onClickListener: OnClickListener? = null
    override fun setOnClickListener(l: OnClickListener?) {
        this.onClickListener = l
        super.setOnClickListener(onClickListener)

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