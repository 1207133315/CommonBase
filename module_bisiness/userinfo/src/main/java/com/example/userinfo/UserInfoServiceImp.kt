package com.example.userinfo

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.alibaba.android.arouter.facade.annotation.Route
import com.liningkang.base.BaseApplication
import com.liningkang.common.RouteConfig
import com.liningkang.common.bean.UserInfo
import com.liningkang.common.interfaces.IUserInfoService
import com.liningkang.common.listener.OnUserInfoUpdateListener
import com.liningkang.ui.R
import com.liningkang.utils.DeviceUtils
import com.liningkang.utils.LogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.jetbrains.annotations.NotNull

@Route(path = RouteConfig.ROUTER_SERVICE_USERINFO)
class UserInfoServiceImp : IUserInfoService {
    private lateinit var userDao: UserDao
    private lateinit var userInfoId: String
    private var listenerList = mutableListOf<OnUserInfoUpdateListener>()
    private var userInfo: UserInfo? = null
    private var scope = CoroutineScope(Dispatchers.IO)
    private val mainHandler:Handler= Handler(Looper.getMainLooper())

    override suspend fun getUserInfoById(@NotNull id: String): UserInfo? {
        userInfo = userDao.getUserInfoById(
            id
        )
        return userInfo
    }

    override fun getUserInfoById(): UserInfo? {
        userInfo = userDao.getUserInfoById(
            DeviceUtils.getAndroidId(
                BaseApplication.context!!
            )
        )
        return userInfo
    }

    override suspend fun insertUserInfo(userInfo: UserInfo?) {
        userDao.insertUserInfo(userInfo)
    }

    override fun update(userInfo: UserInfo?) {
        if (userInfo != null) {
            userDao.update(userInfo)
            mainHandler.post {
                this.userInfo = userInfo
                listenerList.forEach {
                    it?.onUpdate(userInfo)
                }
            }


        }
    }

    override suspend fun delete(userInfo: UserInfo?) {
        userDao.delete(userInfo)
    }

    override suspend fun initUserInfo(context: Context) {
        userDao = UserInfoDatabase.instance.userDao()
        userInfoId = DeviceUtils.getAndroidId(context)
        LogUtils.d("userInfoId", "${userInfoId}")
        // 获取设备唯一id
        userInfo = getUserInfoById()
        // 当数据库没有本用户的时候 代表第一次使用应用 创建用户
        if (userInfo == null) {
            LogUtils.d("userInfoId", "userInfo为空")
            userInfo = UserInfo()
            userInfo?.userId = userInfoId!!

            userDao.insertUserInfo(userInfo)

        }

    }


    private fun getString(id: Int): String {
        return BaseApplication.context!!.resources.getString(id)
    }


    override fun addUserInfoUpdateListener(listener: OnUserInfoUpdateListener?) {
        listener?.apply { listenerList.add(listener) }
    }

    override fun removeUserInfoUpdateListener(listener: OnUserInfoUpdateListener) {
        listener?.apply { listenerList.remove(listener) }
    }

    override fun init(context: Context?) {
        userDao = UserInfoDatabase.instance.userDao()
    }
}