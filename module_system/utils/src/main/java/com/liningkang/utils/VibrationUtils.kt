package com.liningkang.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

object VibrationUtils {
    private   var vibrator:Vibrator?=null
    fun vibrate(context: Context, timings: LongArray, amplitudes: IntArray?=null) {
        if (vibrator==null){
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (amplitudes!=null){
                val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                vibrator?.vibrate(effect)
            }else{
                vibrator?.vibrate(timings[0])
            }
        } else {
            // 兼容之前的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = VibrationEffect.createWaveform(timings, -1)
                vibrator?.vibrate(effect)
            } else {
                // 针对更早的版本，可以使用固定的时长震动
                vibrator?.vibrate(timings[0])
            }
        }
    }
}