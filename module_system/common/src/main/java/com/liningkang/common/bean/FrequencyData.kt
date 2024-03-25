package com.liningkang.common.bean

data class FrequencyData(
    var hourOfDay: Int,
    var minute: Int = 2,
    var notificationDataId:Int=20,
    var type: Int = 1

) {
}