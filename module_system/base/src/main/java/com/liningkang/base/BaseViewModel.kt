package com.liningkang.base

import androidx.lifecycle.*
import com.alibaba.android.arouter.launcher.ARouter
import com.liningkang.common.RouteConfig
import com.liningkang.common.bean.UserInfo
import com.liningkang.common.interfaces.IUserInfoService
import com.liningkang.common.listener.OnUserInfoUpdateListener
import com.liningkang.network.NetworkConfig
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.reflect.ParameterizedType
import kotlin.coroutines.CoroutineContext

open class BaseViewModel<T> : ViewModel(), LifecycleEventObserver, OnUserInfoUpdateListener {

    var userInfoService = ARouter.getInstance().build(RouteConfig.ROUTER_SERVICE_USERINFO)
        .navigation() as? IUserInfoService
    var userInfo: UserInfo = userInfoService!!.getUserInfoById()!!
    var onUserInfoChangeListener: (() -> Unit)? = null


    init {
        userInfoService?.addUserInfoUpdateListener(this)
    }

    override fun onUpdate(userInfo: UserInfo) {
        this@BaseViewModel.userInfo = userInfo
        onUserInfoChangeListener?.invoke()
    }

    /**
     * 在主线程中执行一个协程
     */
    protected fun launchOnUI(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.Main) { block() }
    }

    /**
     * 在IO线程中执行一个协程
     */
    protected fun launchOnIO(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) { block() }
    }

    /**
     * 使用Flow流执行代码块
     */
    protected fun <D> launchOnFlow(
        context: CoroutineContext,
        block: suspend FlowCollector<D>.() -> Unit
    ): Flow<D> {
        return flow { block() }.flowOn(context)
    }

    /**
     * 此方法用来确定采用的Rtrofit中baseUrl
     * 由于Retrofit特性，baseUrl不能随意改动，当大项目拥有
     * 多个域名控制不同业务的时候，则需要不同的Retrofit
     * @TODO 可以根据需要在子类中重写
     */
    protected open fun getRequestType(): Int {
        return NetworkConfig.REQUEST_TYPE_DEFAULT
    }


    open fun start(source: LifecycleOwner) {
    }


    open fun resume(source: LifecycleOwner) {
    }


    open fun pause(source: LifecycleOwner) {
    }


    open fun stop(source: LifecycleOwner) {
    }


    open fun destroy(source: LifecycleOwner) {
        userInfoService?.removeUserInfoUpdateListener(this)
        onUserInfoChangeListener = null
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> {
                start(source)
            }

            Lifecycle.Event.ON_RESUME -> {
                resume(source)
            }

            Lifecycle.Event.ON_PAUSE -> {
                pause(source)
            }

            Lifecycle.Event.ON_STOP -> {
                stop(source)
            }

            Lifecycle.Event.ON_DESTROY -> {
                destroy(source)
            }

            Lifecycle.Event.ON_CREATE -> {

            }

            Lifecycle.Event.ON_ANY -> {}
        }
    }


    /**
     * 请求数据，所有网络操作请使用本方法
     * @param observable
     * @param dataCall
     * @return
     */
    /* suspend fun <D : Any> request(
         call: suspend (service: T) -> DataResponse<D>
     ): ParseResult<D> {
         var parseResult = try {
             withContext(Dispatchers.Main) {
                 customDialog?.show()
             }
             val service = NetworkManager.create(getRequestType(), getTClass<T>())
             val response = call(service)
             withContext(Dispatchers.Main) {
                 customDialog?.dismiss()
             }
             if (response?.isSuccess() == true) {
                 ParseResult.Success(response.data)
             } else {
                 ParseResult.Failure(response.code, response.msg)
             }
         } catch (ex: Throwable) {
             withContext(Dispatchers.Main) {
                 customDialog?.dismiss()
             }
             ParseResult.Error(ex, HttpError.handleException(ex))
         }
         parseResult.procceed()
         return parseResult
     }
 */

    /**
     * 获取泛型对相应的Class对象
     * @return
     */
    fun <T> getTClass(): Class<T>? {
        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        val type = this.javaClass.genericSuperclass as ParameterizedType
        //返回表示此类型实际类型参数的 Type 对象的数组()，想要获取第二个泛型的Class，所以索引写1
        return type.actualTypeArguments[0] as Class<T> //<T>
    }


    /**
     * 返回值类型，方便不同接口返回数据结构不同的情况，参见[.getConsumer]
     * 应对大项目多数据结构
     * @TODO 可以根据需要在子类中重写
     */
      protected fun getResponseType(): Int {
          return NetworkConfig.RESPONSE_TYPE_DEFAULT
      }


}