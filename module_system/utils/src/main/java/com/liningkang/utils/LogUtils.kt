package com.liningkang.utils

import android.util.Log

object LogUtils {
    const val TAG="LogUtils"
    var isDebug = BuildConfig.DEBUG && BuildConfig.IS_LOGABLE

    var isShowLineNumber = false



    /**
     * 获取相关数据:类名,方法名,行号等.用来定位行<br>
     * at cn.utils.MainActivity.onCreate(MainActivity.java:17) 就是用來定位行的代碼<br>
     *
     * @return [ Thread:main, at
     *         cn.utils.MainActivity.onCreate(MainActivity.java:17)]
     */
    private fun getFunctionName(): String {
        var sts = Thread.currentThread().stackTrace
        if (sts != null) {
            for (st in sts) {
                if (st.isNativeMethod) {
                    continue
                }
                if (st.className.equals(Thread::class.java.name)) {
                    continue
                }
                if (st.className.equals(LogUtils::class.java.name)) {
                    continue
                }
                return "[Thread:${Thread.currentThread().name}, at ${st.className}.${st.methodName}(${st.fileName}:${st.lineNumber})]"
            }
        }
        return "null"
    }

    /** 输出格式定义 */
    private fun getMsgFormat(msg: String): String {
        return if (isShowLineNumber) "$msg ${getFunctionName()}" else msg
    }

    fun getTag(): String {
    /*    // 获取调用栈信息
        val stackTrace = Thread.currentThread().stackTrace
        var callingClass:String="LogUtils"
        // 获取调用者的类名
        if (stackTrace.size >= 3) {
          var  callerClassName = stackTrace[2].className
            callingClass =
                callerClassName.substring(callerClassName.lastIndexOf('.') + 1)
        }*/
        return TAG
    }

    fun w(msg: String) {
        if (isDebug) {
            Log.w(getTag(), getMsgFormat(msg))
        }
    }

    fun w(tag: String, msg: String) {
        if (isDebug) {
            Log.w(tag, getMsgFormat(msg))
        }
    }

    fun i(msg: String="") {
        if (isDebug) {
            Log.i(getTag(), getMsgFormat(msg))
        }
    }

     fun i(tag: String="", msg: String="") {
        if (isDebug) {
            Log.i(tag, getMsgFormat(msg))
        }
    }

    fun d(msg: String) {
        if (isDebug) {
            Log.d(getTag(), getMsgFormat(msg))
        }
    }

    fun d(tag: String, msg: String) {
        if (isDebug) {
            Log.d(tag, getMsgFormat(msg))
        }
    }

    fun e(msg: String) {
        if (isDebug) {
            Log.e(getTag(), getMsgFormat(msg))
        }
    }

    fun e(tag: String, msg: String) {
        if (isDebug) {
            Log.e(tag, getMsgFormat(msg))
        }
    }
}