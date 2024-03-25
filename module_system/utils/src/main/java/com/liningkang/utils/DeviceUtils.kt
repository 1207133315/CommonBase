package com.liningkang.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.WindowManager

object DeviceUtils {
    fun getAndroidId(context: Context): String {
        var s =
            Settings.System.getString(context?.contentResolver, Settings.Secure.ANDROID_ID) ?: ""
        LogUtils.i("DeviceUtils", s)
        return s

    }

    fun dp2px(context: Context, dpValue: Float): Float {
        /*   return TypedValue.applyDimension(
               TypedValue.COMPLEX_UNIT_DIP,
               dpValue,
               Resources.getSystem().displayMetrics
           )*/
        var scale = context.resources?.displayMetrics?.density;
        return (dpValue * scale!! + 0.5f)/2
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */

    fun getVersionName(context: Context?): String {
        return try {
            val manager = context?.packageManager
            var info = manager?.getPackageInfo(context?.packageName ?: "", 0)
            info?.versionName ?: ""
        } catch (e: java.lang.Exception) {
            ""
        }

    }


    fun getVersionCode(context: Context?): Int {
        return try {
            val manager = context?.packageManager
            var info = manager?.getPackageInfo(context?.packageName ?: "", 0)
            info?.versionCode ?: 0
        } catch (e: java.lang.Exception) {
            0
        }

    }

    fun getScreenDensity(ctx: Context): Float {
        val dsm = DisplayMetrics()
        (ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(
            dsm
        )
        return dsm.density
    }

    fun getCurrentProcessName(context: Context): String? {
        val pid = Process.myPid()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (activityManager?.runningAppProcesses != null) {
            for (processInfo in activityManager.runningAppProcesses) {
                if (processInfo.pid == pid) {
                    return processInfo.processName
                }
            }
        }
        return null // 如果未找到进程名称
    }


}