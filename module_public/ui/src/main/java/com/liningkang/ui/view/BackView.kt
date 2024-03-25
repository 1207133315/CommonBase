package com.liningkang.ui.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.liningkang.ui.R
import kotlinx.android.synthetic.main.back_view_layout.view.backRes

class BackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var onBackClickListener: OnClickListener? = null
    private var backRes = R.drawable.ic_back_black
    private var rootView: View

    init {
        rootView = inflate(context, R.layout.back_view_layout, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BackView)

        backRes = typedArray.getResourceId(R.styleable.BackView_res, backRes)
        rootView.backRes.setBackgroundResource(backRes)
        typedArray.recycle()
        rootView.backRes.setOnClickListener {
            if (onBackClickListener==null){
                if (context is Activity) {
                    context.finish()

                }
            }else{
                onBackClickListener?.onClick(it)
            }
        }
    }

    fun setBackRes(res: Int) {
        backRes = res
        rootView.backRes.setBackgroundResource(backRes)
    }

    fun setOnBackClickListener(onClickListener: OnClickListener?) {
        onBackClickListener = onClickListener
    }
}