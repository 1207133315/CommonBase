package com.liningkang.base

import android.app.Activity
import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.liningkang.common.RouteConfig


@Interceptor(priority = 4)
class TransitionInterceptor : IInterceptor {

    override fun init(context: Context?) {
     /*   list.add(RouteConfig.ROUTER_ACTIVITY_MAIN)
        list.add(RouteConfig.ROUTER_ACTIVITY_SET_LANGUAGE)
        list.add(RouteConfig.ROUTER_ACTIVITY_WELCOME)
        list.add(RouteConfig.ROUTER_ACTIVITY_TIMER)
        list.add(RouteConfig.ROUTER_ACTIVITY_TIMER_END)
        list.add(RouteConfig.ROUTER_ACTIVITY_RECORD_START)*/
    }

    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        //  val context = postcard?.context
       /* var closeAnim=R.anim.activity_close_out_anim
        if (list.contains(postcard?.path)){
            closeAnim=R.anim.activity_splash_close_out_anim
        }*/

        postcard?.withTransition(R.anim.activity_open_in_anim,R.anim.activity_splash_close_out_anim)
        // 继续路由跳转
        callback?.onContinue(postcard);

    }
}