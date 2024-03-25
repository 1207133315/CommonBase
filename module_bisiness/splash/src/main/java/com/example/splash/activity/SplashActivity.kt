package com.example.splash.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.onsets.PercussionOnsetDetector
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

import com.example.splash.R
import com.liningkang.base.AppManager
import com.liningkang.base.BaseCommonActivity
import com.liningkang.common.IntentKeys
import com.liningkang.common.IntentKeys.SPLASH_FROM_DIALOG_NOTIFY
import com.liningkang.common.IntentKeys.SPLASH_FROM_START
import com.liningkang.common.IntentKeys.SPLASH_FROM_SYSTEM_NOTIFY
import com.liningkang.common.RouteConfig
import com.liningkang.common.bean.NotificationData
import com.liningkang.common.interfaces.INotificationService
import com.liningkang.utils.DelayedTaskUtil
import com.liningkang.utils.LogUtils
import com.liningkang.utils.fromJson
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionListener
import kotlinx.android.synthetic.main.activity_splash.progressBar


@Route(path = RouteConfig.ROUTER_ACTIVITY_SPLASH)
class SplashActivity : BaseCommonActivity() {
    private var splashFrom: Int = SPLASH_FROM_START
    private var isAdLoaded = false
    private var adNumber = 99
    companion object{

        var clapsValue=0
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initView(savedInstanceState: Bundle?) {
        val service =
            ARouter.getInstance().build(RouteConfig.ROUTER_SERVICE_NOTIFICATION)
                .navigation() as? INotificationService
        //service?.startForegroundService(this)

        receiveIntent(intent)
        requestPermission()




    }

    private fun startDispacher() {


        val fromDefaultMicrophone = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)
        val threshold = 6.0 // lower it a bit
        val sensitivity = 80.0

        val mPercussionDetector = PercussionOnsetDetector(22050f, 1024,
            { time, salience ->
                LogUtils.d("SplashActivity", "Clap detected!")

            }, sensitivity, threshold
        )

        fromDefaultMicrophone.addAudioProcessor(mPercussionDetector)
        Thread(fromDefaultMicrophone, "Audio Dispatcher").start()
    }




    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        receiveIntent(intent)
    }

    private fun requestPermission() {
        AndPermission.with(this).permission(Manifest.permission.POST_NOTIFICATIONS,Manifest.permission.RECORD_AUDIO)
            .callback(object : PermissionListener {
                override fun onSucceed(requestCode: Int, grantPermissions: MutableList<String>) {
                    startDispacher()
                   // loadAd()


                }

                override fun onFailed(requestCode: Int, deniedPermissions: MutableList<String>) {

                  //  loadAd()

                }
            })
            .start()
    }

    private var finishCallback = {

    }




    private var jsonString: String? = null
    private fun receiveIntent(intent: Intent?) {
        intent ?: return
        splashFrom = intent.getIntExtra(IntentKeys.SPLASH_FROM, SPLASH_FROM_START)
        jsonString = intent.getStringExtra(IntentKeys.KEY_NOTIFY_DATA)
        if (splashFrom == SPLASH_FROM_START) {

        } else if (splashFrom == IntentKeys.SPLASH_FROM_SYSTEM_NOTIFY) {
            try {
                val notificationData =
                    jsonString?.fromJson<NotificationData>(NotificationData::class.java)
                if (notificationData != null) {


                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else if (splashFrom == IntentKeys.SPLASH_FROM_DIALOG_NOTIFY) {
            val notificationData =
                jsonString?.fromJson<NotificationData>(NotificationData::class.java)
            if (notificationData != null) {


            }
        }
    }

    override fun onViewClick(v: View?) {
        super.onViewClick(v)
        when (v) {


        }
    }

    override fun finish() {
        super.finish()

    }



    private fun next() {
        if (AppManager.isFirstOpenApp(this@SplashActivity)) {

            // 去到选择语言页面
            ARouter.getInstance().build(RouteConfig.ROUTER_ACTIVITY_WELCOME)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                .navigation(this@SplashActivity)
            finish()
            return
        }
        // 直接去主页
        ARouter.getInstance().build(RouteConfig.ROUTER_ACTIVITY_MAIN)
            .withInt(IntentKeys.INTERSTITIAL_AD_NUMBER, adNumber)
            .withString(IntentKeys.KEY_NOTIFY_DATA, jsonString)
            .withInt(
                IntentKeys.SPLASH_FROM,
                splashFrom
            )
            .navigation(this@SplashActivity)

    }

}