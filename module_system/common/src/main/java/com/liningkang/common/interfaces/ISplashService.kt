package com.liningkang.common.interfaces

import android.app.Activity
import com.alibaba.android.arouter.facade.template.IProvider

interface ISplashService:IProvider {

    fun getSplashActivityClassName():String
}