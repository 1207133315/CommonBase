package com.liningkang.common.listener

import com.liningkang.common.bean.UserInfo

interface OnUserInfoUpdateListener {
    fun onUpdate(userInfo: UserInfo)
}