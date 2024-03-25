package com.example.main


import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.liningkang.common.RouteConfig
import com.liningkang.common.interfaces.IMainService
import com.example.main.activity.MainActivity


@Route(path = RouteConfig.ROUTER_SERVICE_MAIN)
class MainServiceImp: IMainService {
    override fun getMainActivityClassName(): String {
        return MainActivity::class.java.name
    }


    override fun init(context: Context?) {
        
    }
}