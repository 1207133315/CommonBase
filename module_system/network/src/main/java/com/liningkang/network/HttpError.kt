package com.liningkang.network

import android.net.ParseException
import com.google.gson.JsonParseException
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

enum class HttpError(val code: Int, val message: String) {
    // 未知错误
    UNKNOWN(-1, "未知错误"),

    // 网络连接错误
    CONNECT_ERROR(-2, "网络连接错误"),

    // 连接超时
    CONNECT_TIMEOUT(-3, "连接超时"),

    // 错误的请求
    BAD_NETWORK(-4, "错误的请求"),

    // 数据解析错误
    PARSE_ERROR(-5, "数据解析错误"),

    // 取消请求
    CANCEL_REQUEST(-6, "取消请求");

    companion object {
        fun handleException(e: Throwable): HttpError {
            e.printStackTrace() //打印异常
            return when (e) {
                is JsonParseException, is JSONException, is ParseException -> {
                    //解析错误
                    PARSE_ERROR
                }
                is ConnectException, is UnknownHostException -> {
                    //网络错误
                    CONNECT_ERROR
                }
                is SocketTimeoutException -> {
                    // 连接超时
                    CONNECT_TIMEOUT
                }
                else -> {
                    //未知错误
                    UNKNOWN
                }
            }
        }
    }


}