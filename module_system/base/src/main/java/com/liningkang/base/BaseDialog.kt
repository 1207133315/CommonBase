package com.liningkang.base

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.lifecycle.Lifecycle.Event.*
import com.liningkang.common.listener.OnDismissListener
import com.liningkang.common.listener.OnShowListener

abstract class BaseDialog(activity: AppCompatActivity, attrId: Int) : Dialog(activity, attrId),
    LifecycleEventObserver ,LifecycleOwner,com.liningkang.common.dialog.Dialog{
    constructor(activity: AppCompatActivity) : this(activity, R.style.fullScreenDialog)
    private var lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    companion object {
        private const val TAG = "BaseDialog"
        private const val ACTION_SHOW = 0
        private const val ACTION_DISMISS = 1
        private const val ACTION_CANCEL = 2
    }
    /**
     * 是否被挤出（每个实现DialogManager.Dialog的窗口类都需要新建该变量）
     */
    protected var isCrowdOut = false
    private val mAction: MutableLiveData<Int> = MutableLiveData()
    val activity = activity
    var view: View? = null

    init {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        val attributes = window?.attributes
        if (attributes!=null){
            attributes.width=LayoutParams.MATCH_PARENT
            attributes.gravity=Gravity.CENTER
            window?.attributes=attributes
        }
        if (activity is LifecycleOwner) {
            mAction.observe(activity as LifecycleOwner) { action ->  // 保证调用show和dismiss不会出错
                Log.d(TAG, "BaseDialog: [activity, attrId action=$action")
                if (action === BaseDialog.ACTION_SHOW) {
                    if (!isShowing && !activity.isFinishing) {
                        Log.d(TAG, "BaseDialog: show $this")
                        super.show()
                    }
                } else if (action === BaseDialog.ACTION_DISMISS) {
                    if (isShowing) {
                        Log.d(TAG, "BaseDialog: dismiss $this")
                        super.dismiss()
                    }
                } else if (action === BaseDialog.ACTION_CANCEL) {
                    if (isShowing) {
                        Log.d(TAG, "BaseDialog: cancel $this")
                        super.cancel()
                    }
                } else {
                    throw IllegalArgumentException("BaseDialog can not do this action $action")
                }
            }
        }
        activity.lifecycle.addObserver(this) // 防止泄漏

        LayoutInflater.from(context).inflate(getLayoutId(), null, false)?.apply {
            setContentView(this)
        }

    }

    lateinit var scaleAnimation: ScaleAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scaleAnimation = ScaleAnimation(
            0f, 1f, // 开始和结束时的宽度比例
            0f, 1f, // 开始和结束时的高度比例
            Animation.RELATIVE_TO_SELF, 0.5f, // X 轴的锚点
            Animation.RELATIVE_TO_SELF, 0.5f // Y 轴的锚点
        ).apply {
            duration = 200 // 动画持续时间
            fillAfter = true // 动画结束后保持最终状态
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
        // 在对话框容器上应用动画
        view?.startAnimation(scaleAnimation)

    }

    override fun onStart() {
        super.onStart()
        // 更新 LifecycleRegistry 的状态
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    override fun onStop() {
        super.onStop()
        // 更新 LifecycleRegistry 的状态

    }


    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
        this@BaseDialog.view=view
    }
    override fun show() {
        Log.d(TAG, "show: []")
        showSafely()
    }

    override fun dismiss() {
        Log.d(TAG, "dismiss: []")
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        dismissSafely()
    }

    private fun showSafely() {
        if (this.activity == null) {
            Log.d(TAG, "showSafely: []")
            return
        }
        mAction.setValue(ACTION_SHOW)
    }


    private fun dismissSafely() {
        if (activity == null) {

            Log.d(TAG, "dismissSafely: []")

            return
        }
        if (activity is LifecycleOwner
            && (activity as LifecycleOwner).lifecycle != null && (activity as LifecycleOwner).lifecycle.currentState.isAtLeast(
                Lifecycle.State.CREATED
            )
        ) {
            mAction.setValue(ACTION_DISMISS)
        } else {
            if (isShowing) {
                super.dismiss()
            }
        }
        activity.lifecycle.removeObserver(this)
    }

    override fun cancel() {
        Log.d(TAG, "cancel: []")
        cancelSafely()
    }

    private fun cancelSafely() {
        if (activity == null) {
            Log.d(TAG, "cancelSafely: return")
            return
        }
        if (activity is LifecycleOwner
            && (activity as LifecycleOwner).lifecycle != null && (activity as LifecycleOwner).lifecycle.currentState.isAtLeast(
                Lifecycle.State.CREATED
            )
        ) {
            mAction.setValue(ACTION_CANCEL)
        } else {
            if (isShowing) {
                super.cancel()
            }
        }
        activity.lifecycle.removeObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            ON_DESTROY -> {
                if (source == activity) {
                    Log.d(TAG, "onActivityDestroyed: [activity]")
                    dismissSafely()
                }
            }
            ON_CREATE -> {

            }
            ON_START -> {

            }
            ON_RESUME -> {

            }
            ON_PAUSE -> {

            }
            ON_STOP -> {

            }
            ON_ANY -> {

            }
        }
    }

    abstract fun getLayoutId(): Int
    override fun setOnDismissListener(listener: OnDismissListener?) {
        setOnDismissListener(DialogInterface.OnDismissListener {
            listener?.onDismiss(isCrowdOut)
        })
    }
    override fun dismiss(isCrowdOut: Boolean) {
        this.isCrowdOut=isCrowdOut
        dismiss()
    }
    override fun isCanShow(): Boolean {
        return true
    }
    override fun setOnShowListener(listener: OnShowListener?) {
       setOnShowListener {
           listener?.onShow()
       }
    }

}