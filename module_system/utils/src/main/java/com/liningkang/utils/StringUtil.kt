package com.liningkang.utils

import com.google.gson.Gson
import java.lang.reflect.Type
import java.util.Locale

/**
 * @Author ；Ningkang.Li
 * @Time ：2023/5/24日 18点
 * @Description ；---
 */
fun Any.toJson(): String {
    return Gson().toJson(this)
}

fun <T> String.fromJson( classOfT: Class<T>): T {
    return Gson().fromJson(this, classOfT)
}



fun <T> String.fromJson(typeOfT: Type): T {
    return Gson().fromJson(this, typeOfT)
}

fun Float.formatOneDecimal():Float{
    return "%.1f".format(Locale.US, this).toFloat()
}

fun Double.formatOneDecimal():Double{
    return "%.1f".format(Locale.US, this).toDouble()
}


