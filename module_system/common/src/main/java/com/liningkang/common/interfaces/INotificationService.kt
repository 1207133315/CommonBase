package com.liningkang.common.interfaces

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

interface INotificationService:IProvider {

    fun sendWork(packageContext: Context, hour: Int, minute: Int, type: Int, notificationDataId: Int)

    fun sendBigNotifyReceiver(notificationDataId: Int)

    fun getTypeEventDialog():Int

    fun stopLastRecordRunnable()
    fun startForegroundService(context:Context)

    fun startBigNotify()
    fun scheduleDailyWork(
        context: Context,
        hourOfDay: Int, minute: Int, type: Int,
        notificationDataId: Int
    )
    fun cancelAllWork()
}