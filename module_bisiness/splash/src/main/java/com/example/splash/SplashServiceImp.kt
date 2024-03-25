package com.example.splash

import android.app.Activity
import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.splash.activity.SplashActivity
import com.liningkang.common.RouteConfig
import com.liningkang.common.interfaces.ISplashService


@Route(path = RouteConfig.ROUTER_SERVICE_SPLASH)
class SplashServiceImp:ISplashService {


    override fun getSplashActivityClassName(): String {
        return SplashActivity::class.java.name
    }

    override fun init(context: Context?) {

    }
}