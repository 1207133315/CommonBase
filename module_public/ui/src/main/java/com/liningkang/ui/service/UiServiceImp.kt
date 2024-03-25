package com.liningkang.ui.service

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import com.alibaba.android.arouter.facade.annotation.Route
import com.liningkang.base.BaseActivity
import com.liningkang.base.BaseApplication
import com.liningkang.common.RouteConfig
import com.liningkang.common.interfaces.IUiService
import com.liningkang.ui.CustomDialog
import com.liningkang.ui.NotificationUtil

@Route(path = RouteConfig.ROUTER_SERVICE_UI)
class UiServiceImp : IUiService {

    override fun createCustomDialog(activity: Activity): Dialog {
        return CustomDialog(activity as BaseActivity)
    }

    override fun startNotificationListenerService() {
        var context=BaseApplication.context?:return
        context.startService(Intent(context,MyNotificationListenerService::class.java))
    }

    override fun sentNotification(
        context: Context,
        intent: Intent,
        titleText: String,
        contentText: String,
        notifyId: Int,
        iconResId: Int,
        isHeadsUp: Boolean
    ) {
        NotificationUtil.notify(context, notifyId ,intent, titleText, contentText,iconResId ,isHeadsUp)
    }

    /**
     * Do your init work in this method, it well be call when processor has been load.
     *
     * @param context ctx
     */
    override fun init(context: Context?) {

    }
}