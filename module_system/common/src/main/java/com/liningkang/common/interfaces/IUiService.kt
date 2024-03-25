package com.liningkang.common.interfaces

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import com.alibaba.android.arouter.facade.template.IProvider

interface IUiService : IProvider{
    fun createCustomDialog(activity: Activity):Dialog

    fun startNotificationListenerService()
    fun sentNotification(
        context: Context,
        intent: Intent,
        titleText: String,
        contentText: String,
        notifyId:Int,
        iconResId:Int,
        isHeadsUp: Boolean
    )
}