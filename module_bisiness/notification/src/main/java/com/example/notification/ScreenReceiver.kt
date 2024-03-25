package com.example.notification

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.WorkManager
import com.alibaba.android.arouter.launcher.ARouter
import com.example.sdks.BuildConfig
import com.liningkang.base.BaseApplication
import com.liningkang.common.IntentKeys
import com.liningkang.common.RouteConfig
import com.liningkang.common.interfaces.INotificationService
import com.liningkang.utils.LogUtils

class ScreenReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "NotificationService"
        var unlockTime=0L
        // 检测屏幕是否是解锁状态
        fun checkScreenUnlock(context: Context): Boolean {
            val keyguardManager =
                context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

            val isScreenLocked = keyguardManager.isKeyguardLocked
            val isScreenOn = powerManager.isInteractive

            return isScreenOn && !isScreenLocked
        }

        // 检测屏幕是否是息屏状态
        fun checkScreenOff(context: Context): Boolean {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            return !powerManager.isInteractive
        }


    }

    private val service =
        ARouter.getInstance().build(RouteConfig.ROUTER_SERVICE_NOTIFICATION)
            .navigation() as? INotificationService

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_SCREEN_ON -> {
                // 屏幕亮屏事件处理
                LogUtils.d(TAG, "屏幕亮屏")
            }

            Intent.ACTION_SCREEN_OFF -> {
                // 屏幕熄屏事件处理
                LogUtils.d(TAG, "屏幕息屏")

                service?.cancelAllWork()
            }

            Intent.ACTION_USER_PRESENT -> {
                // 屏幕解锁
                LogUtils.d(TAG, "屏幕解锁")
                unlockTime=System.currentTimeMillis()
                service?.cancelAllWork()


            }

            Intent.ACTION_TIMEZONE_CHANGED, Intent.ACTION_TIME_CHANGED -> {

                /*if (BuildConfig.DEBUG){
                    LogUtils.d(TAG, "系统时间修改")
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(BaseApplication.context, AlarmReceiver::class.java)
                        intent.action = AlarmReceiver.ACTION_FREQUENCY_NOTIFICATION
                        BaseApplication.context?.sendBroadcast(intent)
                    }, 3000)
                }*/

            }

        }
    }

    fun register(context: Context) {
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_SCREEN_ON)
        val receivers =
            packageManager.queryBroadcastReceivers(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (receivers == null || receivers.isEmpty()) {
            val intentFilter = IntentFilter()

            intentFilter.addAction(Intent.ACTION_SCREEN_ON)
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
            intentFilter.addAction(Intent.ACTION_USER_PRESENT)
            intentFilter.addAction(Intent.ACTION_TIME_CHANGED)
            intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED)

            context.registerReceiver(this, intentFilter)
        }


    }
}