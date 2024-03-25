package com.liningkang.network

class NetworkConfig {
    companion object {

        const val REQUEST_TYPE_DEFAULT = 0 //默认IRequest

        const val REQUEST_TYPE_SDK_BD = 100 //例：这个为请求百度的接口，请求结构为另外一种,主要是应对不同域名的情况

        const val RESPONSE_TYPE_DEFAULT = 0

        const val RESPONSE_TYPE_SDK_BD = 100 ////例：这个为请求百度的接口，接口结构为另外一种

        const val BASE_URL = "http://wthrcdn.etouch.cn/"

        const val HTTP_OK = 1000 // 请求成功状态

    }

}