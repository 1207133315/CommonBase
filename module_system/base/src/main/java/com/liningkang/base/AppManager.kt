package com.liningkang.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Process
import android.util.Log
import com.tencent.mmkv.MMKV
import java.util.Stack

object AppManager {
    private var SP_NAME = "SP_"

    /**
     * 维护activity的栈结构
     */
    var mActivityStack: Stack<Activity?>? = null

    private const val KEY_OPEN_APP_COUNT = "key_open_app_count"

    private var context: Context? = null
    var openAppCount = 0
    private var sharedPreferences: SharedPreferences? = null

    /**
     * 记录app打开的次数
     *
     * @param
     */
    fun init(context: Context) {

        AppManager.context = context.applicationContext
        if (sharedPreferences == null) {
            SP_NAME = "SP_"
            SP_NAME += context.applicationContext.packageName
            sharedPreferences =
                context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        }

        openAppCount = sharedPreferences?.getInt(KEY_OPEN_APP_COUNT, 0) ?: 0
        openAppCount++
        sharedPreferences?.edit()?.putInt(KEY_OPEN_APP_COUNT, openAppCount)?.commit()

    }

    fun getTopActivity(): Activity? {
        if (mActivityStack != null && (mActivityStack?.size ?: 0) > 0) {
            return mActivityStack?.get((mActivityStack?.size ?: 0) - 1)
        }
        return null
    }

    fun getActivityCount(): Int {

        return mActivityStack?.size ?: 0

    }

    fun isFirstOpenApp(context: Context): Boolean {
        if (sharedPreferences == null) {
            SP_NAME = "SP_"
            SP_NAME += context.applicationContext.packageName
            sharedPreferences =
                context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        }
        openAppCount = sharedPreferences?.getInt(KEY_OPEN_APP_COUNT, 0) ?: 0
        return openAppCount == 0
    }


    fun resetOpenAppCount() {
        if (sharedPreferences == null) {
            SP_NAME = "SP_"
            SP_NAME += context?.applicationContext?.packageName
            sharedPreferences =
                context?.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        }
        sharedPreferences?.edit()?.putInt(KEY_OPEN_APP_COUNT, 0)?.commit()
    }


    /**
     * 添加Activity到堆栈
     *
     * @param activity activity实例
     */
    fun addActivity(activity: Activity?) {
        if (mActivityStack == null) {
            mActivityStack = Stack()
        }
        if (!mActivityStack!!.contains(activity)){
            mActivityStack!!.add(activity)
        }

    }

    /**
     * 获取当前Activity（栈中最后一个压入的）
     *
     * @return 当前（栈顶）activity
     */
    fun currentActivity(): Activity? {
        return if (mActivityStack != null && !mActivityStack!!.isEmpty()) {
            mActivityStack!!.lastElement()
        } else null
    }

    /**
     * 结束除当前activtiy以外的所有activity
     * 注意：不能使用foreach遍历并发删除，会抛出java.util.ConcurrentModificationException的异常
     *
     * @param activity 不需要结束的activity
     */
    fun finishOtherActivity(activity: Activity) {
        if (mActivityStack != null) {
            val it: Iterator<Activity?> = mActivityStack!!.iterator()
            while (it.hasNext()) {
                val temp = it.next()
                if (temp != null && temp !== activity) {
                    finishActivity(temp)
                }
            }
        }
    }

    /**
     * 结束除这一类activtiy以外的所有activity
     * 注意：不能使用foreach遍历并发删除，会抛出java.util.ConcurrentModificationException的异常
     *
     * @param cls 不需要结束的activity
     */
    @SuppressLint("StaticFieldLeak")
    fun finishOtherActivity(cls: Class<*>) {
        if (mActivityStack != null) {
            val it: Iterator<Activity?> = mActivityStack!!.iterator()

            while (it.hasNext()) {
                val activity = it.next()
                if (activity!!.javaClass != cls) {
                    finishActivity(activity)
                }
            }
        }
    }


    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        if (mActivityStack != null && !mActivityStack!!.isEmpty()) {
            val activity = mActivityStack!!.lastElement()
            finishActivity(activity)
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param activity 指定的activity实例
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            if (mActivityStack != null && mActivityStack!!.contains(activity)) { // 兼容未使用AppManager管理的实例
                mActivityStack!!.remove(activity)
            }
            activity.finish()
        }
    }

    /**
     * 结束指定类名的所有Activity
     *
     * @param cls 指定的类的class
     */
    fun finishActivity(cls: Class<*>) {
        if (mActivityStack != null) {
            val it: Iterator<Activity?> = mActivityStack!!.iterator()
            while (it.hasNext()) {
                val activity = it.next()
                if (activity!!.javaClass == cls) {
                    finishActivity(activity)
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        if (mActivityStack != null) {
            var i = 0
            val size = mActivityStack!!.size
            while (i < size) {
                if (null != mActivityStack!![i]) {
                    mActivityStack!![i]!!.finish()
                }
                i++
            }
            mActivityStack!!.clear()
        }
    }

    fun minimizeApp(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)
        context.startActivity(intent)
    }

    fun removeActivity(activity: Activity?) {
        if (activity != null) {
            if (mActivityStack != null && mActivityStack!!.contains(activity)) { // 兼容未使用AppManager管理的实例
                mActivityStack!!.remove(activity)
            }
        }
    }

    /**
     * 退出应用程序
     */
    fun exitApp() {
        try {
            finishAllActivity()
            // 退出appM(java虚拟机),释放所占内存资源,0表示正常退出(非0的都为异常退出)
            System.exit(0)
            // 从操作系统中结束掉当前程序的进程
            Process.killProcess(Process.myPid())
        } catch (e: Exception) {
        }
    }


}