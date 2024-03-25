package com.liningkang.ui

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.liningkang.base.BaseApplication
import com.liningkang.ui.NotificationUtil.channelID

object NotificationUtil {
    const val KEY_NOTIFY_DATA = "key_notify_data"
    const val KEY_NOTIFY_ID = "key_notify_id"
    private var notificationManager: NotificationManager =
        BaseApplication.context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    var channelID = "1"
    var riskChannelID = "2"
    private var channelName = BaseApplication.context!!.resources.getString(R.string.app_name)
    private val gson = Gson()
    var notifyIntentList = mutableListOf<NotificationIntent>()
    private var notificationChannel: NotificationChannel? = null
    private var riskNotificationChannel: NotificationChannel? = null

    init {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //参数1：channel的ID； 参数2：channel的name:； 参数3：通知Channel的级别（重要程度）
            notificationChannel =
                NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            riskNotificationChannel =
                NotificationChannel(
                    riskChannelID,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                );
            notificationManager.createNotificationChannel(notificationChannel!!)
            notificationManager.createNotificationChannel(riskNotificationChannel!!)
        }
    }


    @SuppressLint("RemoteViewLayout")
    fun notify(
        context: Context,
        notifyId: Int,
        intent: Intent,
        titleText: String,
        contentText: String,
        iconResId: Int,
        isHeadsUp: Boolean = true,
    ) {
        var smallRemoteViews = RemoteViews(context.packageName, R.layout.shrink_notify_layout)
        smallRemoteViews.setTextViewText(R.id.titleText, titleText)


        var bigRemoteViews = RemoteViews(context.packageName, R.layout.unfold_notify_layout)
        bigRemoteViews.setTextViewText(R.id.titleText, titleText)
        bigRemoteViews.setImageViewResource(R.id.iconView, iconResId)

        var notification =
            createNotification(
                context,
                titleText,
                contentText,
                intent,
                notifyId,
                smallRemoteViews,
                bigRemoteViews,
                isHeadsUp,
            )


        // 判断list中不包含再添加 如何已包含就不添加了
        notifyIntentList.find { it.notifyId == notifyId }
            ?: notifyIntentList.add(NotificationIntent(notifyId, intent, notification))
        notificationManager.notify(notifyId, notification)
    }

    fun createNotification(
        context: Context,
        titleText: String,
        contentText: String,
        intent: Intent?,
        notifyId: Int,
        smallRemoteViews: RemoteViews? = null,
        bigRemoteViews: RemoteViews? = null,
        isHeadsUp: Boolean = true,
        isOngoing: Boolean = false
    ): Notification {
        var notification =
            NotificationCompat.Builder(context, if (isHeadsUp) channelID else riskChannelID)

                .setContentTitle(context.resources.getString(R.string.app_name))
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.notify_logo)
                .setWhen(System.currentTimeMillis())
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setDefaults(NotificationCompat.DEFAULT_ALL)//设置系统默认的声音，震动等效果
                .setPriority(if (isHeadsUp) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT)//设置通知的优先级（重要程度）
                .setOngoing(isOngoing)
                .setAutoCancel(true)
        if (intent != null) {
            intent?.putExtra(KEY_NOTIFY_ID, notifyId)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val pendingIntent =
                PendingIntent.getActivity(
                    context,
                    notifyId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            notification.setContentIntent(pendingIntent)
        }
        if (smallRemoteViews != null) {
            notification
                .setCustomContentView(smallRemoteViews)    //折叠后通知显示的布局
                .setCustomBigContentView(bigRemoteViews)    //折叠后通知显示的布局
                .setCustomHeadsUpContentView(bigRemoteViews)//横幅样式显示的布局
                .setContent(smallRemoteViews)
        }


        return notification.build()
    }


    /**
     * 改变通知栏中的通知的文案语言
     * 切换语言时把通知里的文案语言也切换了
     */
    fun changeNotifyLanguage() {
        /*  val service = ARouter.getInstance().build(RouteConfig.ROUTER_SERVICE_MAIN)
              .navigation() as? IMainService
          notifyIntentList.toMutableList().forEach {
              val json = it.intent.getStringExtra(KEY_NOTIFY_DATA) ?: return
              val notifyData = gson.fromJson(json, NotificationData::class.java) ?: return
              val notifyDataList = service?.getNotifyDataList()
              notifyDataList?.filter { it.id == notifyData.id }?.forEach { notificationData ->

                  service?.sendNotifyByNotificationData( notificationData)
              }
          }*/
    }


    data class NotificationIntent(
        val notifyId: Int,
        val intent: Intent,
        val notification: Notification
    )
}