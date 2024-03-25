package com.liningkang.ui.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.liningkang.ui.NotificationUtil
import com.liningkang.utils.LogUtils

class MyNotificationListenerService: NotificationListenerService() {
    override fun onCreate() {
        super.onCreate()
        LogUtils.d("AlarmService","通知监听被启动")
    }
    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        val id = sbn?.id
        val notifyIntentList = NotificationUtil.notifyIntentList
        LogUtils.d("AlarmService","通知被取消")
        notifyIntentList.removeAll { it.notifyId==id  }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?, rankingMap: RankingMap?) {
        super.onNotificationRemoved(sbn, rankingMap)
    }

}