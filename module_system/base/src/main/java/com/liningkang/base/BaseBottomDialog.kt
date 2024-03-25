package com.liningkang.base

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

/**
 * @Author ；Ningkang.Li
 * @Time ：2023/5/22日 11点
 * @Description ；---
 */
abstract class BaseBottomDialog(context: BaseActivity,themResId:Int) :
    Dialog(context,themResId),
    LifecycleEventObserver, LifecycleOwner {
    var activity=context
    private var lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    constructor(context: BaseActivity):this(context, R.style.Theme_BottomDialog_Base)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRegistry.handleLifecycleEvent( Lifecycle.Event.ON_CREATE)
        setContentView(getLayoutResId())
        initView()
        /*setOnDismissListener {
            lifecycleRegistry.handleLifecycleEvent( Lifecycle.Event.ON_DESTROY)
            lifecycleRegistry.removeObserver(this)
        }*/
    }



    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
    override fun onStart() {
        super.onStart()
        lifecycleRegistry.handleLifecycleEvent( Lifecycle.Event.ON_START)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, getPeekHeight())
        window?.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
    }

    override fun onStop() {
        super.onStop()
        lifecycleRegistry.handleLifecycleEvent( Lifecycle.Event.ON_STOP)
    }


    protected abstract fun getLayoutResId(): Int

    protected abstract fun initView()

    protected open fun getPeekHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }
}