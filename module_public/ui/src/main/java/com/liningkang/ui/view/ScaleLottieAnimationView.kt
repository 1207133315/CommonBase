package com.liningkang.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.WindowManager
import com.airbnb.lottie.LottieAnimationView
import com.liningkang.base.BaseApplication
import com.liningkang.utils.LogUtils


/**
 * @Author ；Ningkang.Li
 * @Time ：2023/4/12日 13点
 * @Description ；---
 */
class ScaleLottieAnimationView(context: Context,attributeSet: AttributeSet?,defStyleAttr:Int):LottieAnimationView(context,attributeSet,defStyleAttr) {
    constructor(context: Context,attributeSet: AttributeSet?):this(context,attributeSet,0)
    constructor(context: Context):this(context,null,0)
    init {
        post {
            var scale=70f* getScreenDensity(BaseApplication.context!!)
            if (width>0){
              //  this@ScaleLottieAnimationView.scale=width/scale
            }
/*            LogUtils.i(
                "Game2048Activity",
                "onResult: 动画缩放比例:" + scale + "--View宽度:" + animation.width
            )*/
        }
    }
    fun getScreenDensity(ctx: Context): Float {
        val dsm = DisplayMetrics()
        (ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(
            dsm
        )
        return dsm.density
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        LogUtils.d("ScaleLottieAnimationView","View关闭")
        cancelAnimation()
        clearAnimation()
    }
}