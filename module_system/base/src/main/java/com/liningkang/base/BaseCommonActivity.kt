package com.liningkang.base

import android.os.Bundle
import org.greenrobot.eventbus.EventBus

/**
 * @description 普通Activity基类
 */
abstract class BaseCommonActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getLayoutId()!=0){
            setContentView(getLayoutId())
            initView(savedInstanceState)
        }
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }





}