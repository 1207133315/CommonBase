package com.liningkang.common.interfaces

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider
import com.liningkang.common.bean.UserInfo
import com.liningkang.common.listener.OnUserInfoUpdateListener

interface IUserInfoService : IProvider {
    suspend fun getUserInfoById(id: String): UserInfo?
    fun getUserInfoById(): UserInfo?
    suspend fun insertUserInfo(userInfo: UserInfo?)
     fun update(userInfo: UserInfo?)
    suspend fun delete(userInfo: UserInfo?)
    suspend fun initUserInfo(context: Context)
    fun addUserInfoUpdateListener(listener: OnUserInfoUpdateListener?)
    fun removeUserInfoUpdateListener(listener: OnUserInfoUpdateListener)
}