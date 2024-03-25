package com.liningkang.common.bean

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class NotificationData(
    /**
     * 0 测心率
     * 1 测心率
     * 2 测血压
     * 3 测血糖
     * 4 记录体重
     * 5 心率追踪
     * 6 心率趋势
     * 7 血压趋势
     * 8 血糖趋势
     * 9 体重趋势
     * 10 健康科普
     * 11 指标趋势
     * 12 清晨通知
     * 13 日间通知
     * 14 晚间通知
     *
     * 其他 健康科普详情
     */
    var id: Int = 0,
    @DrawableRes
    var iconRes: Int,

    var titleResName: String,

    var contentResName: String,

    @DrawableRes
    var bgResId:Int,

    var type: Int = 0,// 0是各个页面  1是健康科普中的详情 2 是早中晚的通知

) {
}