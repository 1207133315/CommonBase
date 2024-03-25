package com.liningkang.base

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Process
import com.alibaba.android.arouter.launcher.ARouter
import com.example.languagelib.MultiLanguageUtil
import com.lky.toucheffectsmodule.TouchEffectsManager
import com.lky.toucheffectsmodule.types.TouchEffectsViewType
import com.lky.toucheffectsmodule.types.TouchEffectsWholeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseApplication : Application() {
    companion object {
        /**
         * 主线程ID
         */
        var mMainThreadId = -1

        /**
         * 主线程ID
         */
        var mMainThread: Thread? = null

        /**
         * 主线程Handler
         */
        var mMainThreadHandler: Handler? = null

        /**
         * 主线程Looper
         */
        var mMainLooper: Looper? = null


        /**
         * context 全局唯一的上下文
         */
        var context: Context? = null

    }

    init {
        // NONE //不自动为控件添加效果
        // SCALE //添加缩放效果
        //  RIPPLE //添加水波纹效果
        //  STATE //添加渐变响应效果
        TouchEffectsManager.build(TouchEffectsWholeType.SCALE)//设置全局使用哪种效果
            .addViewType(TouchEffectsViewType.ALL)
            .addViewTypes()

    }

    override fun attachBaseContext(base: Context?) {
        MultiLanguageUtil.getInstance().saveSystemCurrentLanguage(base)
        super.attachBaseContext(base)
        //app刚启动getApplicationContext()为空
        MultiLanguageUtil.getInstance().setConfiguration(applicationContext)
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //app刚启动不一定调用onConfigurationChanged
        MultiLanguageUtil.getInstance().setConfiguration(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        mMainThreadId = Process.myTid()
        mMainThread = Thread.currentThread()
        mMainThreadHandler = Handler()
        mMainLooper = mainLooper
        MultiLanguageUtil.getInstance().setConfiguration(this)



    }



    override fun onTerminate() {
        super.onTerminate()
        ARouter.getInstance().destroy()
    }

}