package com.liningkang.common.interfaces

import android.app.Activity
import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider
import kotlin.reflect.KClass

interface IMainService:IProvider {
    fun  getMainActivityClassName():String
}