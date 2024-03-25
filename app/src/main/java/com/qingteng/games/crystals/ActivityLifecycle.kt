package com.qingteng.games.crystals

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import com.fengmalisa.commercialization.AdType

import com.example.sdks.AdjustManager
import com.fengmalisa.analytics.AnalyticsConstant
import com.liningkang.utils.CommSharedUtil
import com.liningkang.utils.LogUtils

object ActivityLifecycle : Application.ActivityLifecycleCallbacks {
    private var activityResumedCount = 0
    private var isAppOnBackground = false
    private var activityCreateCount = 0
    private var blacklist = mutableListOf<String>()

    init {
       // blacklist.add(InputPasswordActivity::class.java.simpleName)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {


    }

    override fun onActivityStarted(activity: Activity) {
        if (!blacklist.contains(activity.javaClass.simpleName)) {
            LogUtils.d("MainApplication", "onActivityStarted:" + activity.javaClass.simpleName)
            activityResumedCount++
            if (isAppOnBackground) {
                isAppOnBackground = false
                LogUtils.d("MainApplication", "app热启动")
                val handler = Handler()
                handler.postDelayed({
                    showAppOpenAd(activity)
                }, 1000)


            }
            AdjustManager.trackEvent(
                AnalyticsConstant.EVENT_PAGE_OPEN,
                activity.javaClass.simpleName
            )
        }
        //     val foregroundActivityRef = WeakReference(activity)


    }



    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        if (!blacklist.contains(activity.javaClass.simpleName)) {
            LogUtils.d("MainApplication", "onActivityStopped:" + activity.javaClass.simpleName)
            activityResumedCount--
            if (activityResumedCount <= 0) {
                LogUtils.d("MainApplication", "app进入后台")
                isAppOnBackground = true
            }
        }

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        if (!blacklist.contains(activity.javaClass.simpleName)) {
            activityCreateCount--
        }

    }

    private fun showAppOpenAd(activity: Activity, callback: (() -> Unit?)? = null) {
        var lastAppOpenTime =
            CommSharedUtil.getLong(CommSharedUtil.KEY_APP_OPEN_TIME)
        var interval = 1000*30
        var adNumber = 99
        if (System.currentTimeMillis() - lastAppOpenTime > interval) {
            EventUtils.onAdPvEvent(
                AdjustManager.AD_TYPE_APP_OPEN,
                "热启动进入app"
            )
            if (AdjustManager.readyToServe(AdType.APP_OPEN)) {
                LogUtils.d("MainApplication", "已有开屏广告")
                AdjustManager.showAd(
                    activity,
                    adNumber,
                    AdjustManager.AD_TYPE_APP_OPEN,
                    onAdShowed = {
                        LogUtils.d("AdJustManager", "展示成功,广告id")
                        CommSharedUtil
                            .putLong(CommSharedUtil.KEY_APP_OPEN_TIME, System.currentTimeMillis())
                        AdjustManager.loadAd(
                            activity.applicationContext,
                            adNumber,
                            AdjustManager.AD_TYPE_APP_OPEN
                        )
                    })

            } else {
                LogUtils.d("MainApplication", "无开屏广告,加载")
                AdjustManager.loadAd(
                    activity.applicationContext,
                    adNumber,
                    AdjustManager.AD_TYPE_APP_OPEN
                ) {
                    AdjustManager.showAd(
                        activity,
                        adNumber,
                        AdjustManager.AD_TYPE_APP_OPEN,
                        onAdShowed = {
                            LogUtils.d("AdJustManager", "展示成功,广告id")
                            CommSharedUtil.putLong(
                                CommSharedUtil.KEY_APP_OPEN_TIME,
                                System.currentTimeMillis()
                            )
                        })
                    AdjustManager.loadAd(
                        activity.applicationContext,
                        adNumber,
                        AdjustManager.AD_TYPE_APP_OPEN
                    )
                }
            }

        }


        callback?.invoke()
    }
}