package com.liningkang.utils

import android.os.Handler
import android.os.Looper

object DelayedTaskUtil {
    private val handler: Handler = Handler(Looper.getMainLooper())
    interface DelayedTaskCallback {
        fun onTaskExecuted()
    }
    // 使用 () -> Unit 作为 lambda 表达式的类型
     fun  performDelayedTask(delayMillis: Long, callback: () -> Unit) {
        handler.postDelayed(callback, delayMillis)
    }

    // 取消任务时不再需要提供回调对象
    fun cancelDelayedTask() {
        handler.removeCallbacksAndMessages(null)
    }
}