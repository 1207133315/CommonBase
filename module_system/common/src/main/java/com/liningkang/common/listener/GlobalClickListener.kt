package com.liningkang.common.listener

import android.os.SystemClock
import android.util.Log
import android.view.View

class GlobalClickListener(var listener: View.OnClickListener? = null) : View.OnClickListener {
    private val CLICK_DELAY = 1000 // 设置点击间隔时间（毫秒）
    private var lastClickTime: Long = 0
    override fun onClick(view: View) {
        val currentTime = SystemClock.uptimeMillis()
        if (currentTime - lastClickTime >= CLICK_DELAY) {
            // 处理点击事件
            listener?.onClick(view)
            Log.i("hook", "onClick: hook成功")
            lastClickTime = currentTime
        }
    }


}