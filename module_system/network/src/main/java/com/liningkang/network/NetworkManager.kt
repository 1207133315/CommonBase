package com.liningkang.network

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.Int

/**
 * @author dingtao
 * @date 2018/12/28 10:07
 * qq:1940870847
 */
object NetworkManager{
    private var app_retrofit: Retrofit? = null
    private var baidu_retrofit: Retrofit? = null
    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY //打印请求参数，请求结果
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)//错误重连
            .build()

        app_retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(NetworkConfig.BASE_URL) //base_url:http+域名
          //  .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //使用Rxjava对回调数据进行处理
            .addConverterFactory(GsonConverterFactory.create()) //响应结果的解析器，包含gson，xml，protobuf
            .build()
        baidu_retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(NetworkConfig.BASE_URL) //base_url:http+域名
           // .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //使用Rxjava对回调数据进行处理
            .addConverterFactory(GsonConverterFactory.create()) //响应结果的解析器，包含gson，xml，protobuf
            .build()
    }

    fun <T> create(requestType: Int, service: Class<T>?): T {
        return if (requestType == NetworkConfig.REQUEST_TYPE_SDK_BD) { //如果请求百度SDK的接口
            baidu_retrofit!!.create(service)
        } else app_retrofit!!.create(service)
    }


}