package com.liningkang.base

object FastClick {
    // 两次点击间隔不能少于1000ms
    private const val FAST_CLICK_DELAY_TIME = 500
    private var lastClickTime = 0L

    fun isFastClick(): Boolean {
        var flag = true
        var currentClickTime = System.currentTimeMillis()
        if ((currentClickTime - lastClickTime) >= FAST_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime
        return flag
    }
}