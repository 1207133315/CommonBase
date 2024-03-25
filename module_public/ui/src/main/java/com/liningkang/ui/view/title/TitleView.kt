package com.liningkang.ui.view.title

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.liningkang.ui.R

import kotlinx.android.synthetic.main.title_layout.view.backView
import kotlinx.android.synthetic.main.title_layout.view.mainView
import kotlinx.android.synthetic.main.title_layout.view.skipRes
import kotlinx.android.synthetic.main.title_layout.view.skipText
import kotlinx.android.synthetic.main.title_layout.view.titleText

class TitleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var onSkipClickListener: OnClickListener? = null
    var isShowBack = true
        set(value) {
            field=value
            parentView.backView.visibility = if (isShowBack) View.VISIBLE else View.INVISIBLE
        }
    var isShowSkip = true
        set(value) {
            field=value
            parentView.skipText.visibility = if (isShowSkip) View.VISIBLE else View.INVISIBLE
            parentView.skipRes.visibility = if (isShowSkip) View.VISIBLE else View.INVISIBLE
        }
    var titleText = "Title"
        set(value) {
            field=value
            parentView.titleText.text=value
        }
    var skipText = context.resources.getString(R.string.skip)
        set(value) {
            field=value
            parentView?.skipText?.text=field
        }
    var backRes = R.drawable.ic_back_black
    var skipRes = 0

    var skipTextColor = resources.getColor(R.color._3d8aff)
        set(value) {
            field=value
            parentView.skipText.setTextColor(field)
        }

    var parentView: View

    init {
        parentView = inflate(context, R.layout.title_layout, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleView)
        isShowBack = typedArray.getBoolean(R.styleable.TitleView_isShowBack, isShowBack)
        isShowSkip = typedArray.getBoolean(R.styleable.TitleView_isShowSkip, isShowSkip)
        titleText = typedArray.getString(R.styleable.TitleView_titleText) ?: titleText
        backRes = typedArray.getResourceId(R.styleable.TitleView_backRes, backRes)
        skipRes = typedArray.getResourceId(R.styleable.TitleView_skipRes, 0)
        skipText = typedArray.getString(R.styleable.TitleView_skipText) ?: skipText
        skipTextColor = typedArray.getColor(R.styleable.TitleView_skipTextColor, skipTextColor)
        parentView.backView.visibility = if (isShowBack) View.VISIBLE else View.INVISIBLE
        parentView.titleText.text = titleText
        parentView.backView.setBackRes(backRes)
        parentView.skipText.text = skipText
  //      parentView.mainView.setBackgroundColor(bgColor)
        if (isShowSkip) {
            if (skipRes != 0) {
                parentView.skipText.visibility = View.GONE
                parentView.skipRes.setBackgroundResource(skipRes)
                parentView.skipRes.visibility = View.VISIBLE
            } else {
                parentView.skipText.visibility = View.VISIBLE
                parentView.skipRes.visibility = View.GONE
            }
        } else {
            parentView.skipText.visibility = View.GONE
            parentView.skipRes.visibility = View.GONE
        }

        parentView.skipText.setTextColor(skipTextColor)

        typedArray.recycle()
        parentView.skipText.setOnClickListener {
            onSkipClickListener?.onClick(it)
        }
        parentView.skipRes.setOnClickListener {
            onSkipClickListener?.onClick(it)
        }

    }

    fun setOnSkipClickListener(onClickListener: OnClickListener?) {
        onSkipClickListener = onClickListener

    }

    fun setOnBackClickListener(onClickListener: OnClickListener?) {
        parentView.backView.setOnBackClickListener(onClickListener)
    }
}