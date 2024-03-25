package com.qingteng.games.crystals


import android.os.Build

import com.alibaba.android.arouter.launcher.ARouter

import com.example.notification.ScreenReceiver
import com.example.sdks.AdjustManager

import com.liningkang.base.BaseApplication
import com.liningkang.base.BuildConfig
import com.liningkang.common.RouteConfig
import com.liningkang.common.interfaces.IUiService
import com.liningkang.common.interfaces.IUserInfoService
import com.liningkang.utils.CommSharedUtil
import com.liningkang.utils.DeviceUtils
import com.liningkang.utils.ToastUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            if (getProcessName() == packageName) {
                init()
            }
        }else{
            val processName = DeviceUtils.getCurrentProcessName(this)
            if (processName==null){
                init()
            }else if ( processName==packageName){
               init()
           }
        }

        // KeepAliveManager.init(this)



    }

    private fun init() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog() // Print log
            ARouter.openDebug()
            ARouter.printStackTrace()
        }
        ARouter.init(this) //阿里路由初始化
        ToastUtils.init(this)
        CommSharedUtil.initialize(this)
        registerScreenReceiver()
        FirebaseManager.init(this)
        AdjustManager.init(this)
        initUserInfo() {
          //  AchieveUtil.initAchieveData()

        }

        NotificationService.loadNotifyData {
            //  NotificationService.startSmallNotify()

        }

        /*
                // 主线程
  NotificationService.initBatistaConfig(this)
                KeepAliveManager.init(this)
                //startNotificationListenerService()
             */

    /*    Ads.attachAppContext(
            applicationContext, testDevices = listOf(
                "25FA7C98DE9D650AFB9CA932E70AC539",
                "4FB8D4E9CEC600892111166912C8662F",
               // "54AC9535EF214A2CADE4023E5C3988B5",// s23
               // "B306A3B6C2264CB18969C788FBE588F6", // a54
                "e636f0138b9f4d3c857bddc38a47cd40"// 测试机a51
            ), isTesting = BuildConfig.DEBUG
        )*/

        registerActivityLifecycleCallbacks(ActivityLifecycle)

        try {
            var c = Class.forName("java.lang.Daemons")
            var maxField = c.getDeclaredField("MAX_FINALIZE_NANOS")
            maxField.isAccessible = true
            maxField.set(null, Long.MAX_VALUE)
        } catch (e: Exception) {
            e.printStackTrace();
        }


    }


    private fun registerScreenReceiver() {
        val screenReceiver = ScreenReceiver()
        screenReceiver.register(this)
    }

    private fun initUserInfo(callback: () -> Unit?) {
        val userInfoService = ARouter.getInstance().build(RouteConfig.ROUTER_SERVICE_USERINFO)
            .navigation() as? IUserInfoService?
        CoroutineScope(Dispatchers.IO).launch {
            userInfoService?.initUserInfo(context!!)
            withContext(Dispatchers.Main) {
                callback?.invoke()
            }
        }
    }


    private fun startNotificationListenerService() {
        val service =
            ARouter.getInstance().build(RouteConfig.ROUTER_SERVICE_UI).navigation() as? IUiService
        service?.startNotificationListenerService()
    }


}