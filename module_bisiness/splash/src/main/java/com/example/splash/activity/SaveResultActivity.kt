package com.example.splash.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

import com.example.splash.R
import com.example.splash.data.SaveResultData
import com.example.splash.adapter.SaveResultAdapter
import com.liningkang.base.AppManager
import com.liningkang.base.BaseCommonActivity
import com.liningkang.common.Constants
import com.liningkang.common.IntentKeys

import com.liningkang.common.RouteConfig
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.activity_save_result.progressText

import kotlinx.android.synthetic.main.activity_save_result.recyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Route(path = RouteConfig.ROUTER_ACTIVITY_SAVE_RESULT)
class SaveResultActivity : BaseCommonActivity() {
    companion object {
        var animationDuration = 1500L
    }

    private var itemList = mutableListOf<SaveResultData>()

    lateinit var saveResultAdapter: SaveResultAdapter

    private var outTime = animationDuration * 5
    override fun getLayoutId(): Int {
        return R.layout.activity_save_result
    }

    private var job: Job? = null
    var count = 0
    private val loadHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            count++
            if (count == 6) {
                // 结束

                    ARouter.getInstance().build(RouteConfig.ROUTER_ACTIVITY_MAIN)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .withInt(IntentKeys.SPLASH_FROM, IntentKeys.SPLASH_FROM_GUIDE)
                        .withInt(IntentKeys.INTERSTITIAL_AD_NUMBER, 3)
                        .navigation(this@SaveResultActivity)
                    finish()
                return
            }
            if (count == 1) {
                saveResultAdapter.add(
                    saveResultAdapter.dataList.size, SaveResultData(
                        true, resources.getString(com.liningkang.ui.R.string.save_guide_result_2)
                    )
                )
            } else if (count == 2) {
                saveResultAdapter.add(
                    saveResultAdapter.dataList.size, SaveResultData(
                        true, resources.getString(com.liningkang.ui.R.string.save_guide_result_3)
                    )
                )
            } else if (count == 3) {
                saveResultAdapter.add(
                    saveResultAdapter.dataList.size, SaveResultData(
                        true, resources.getString(com.liningkang.ui.R.string.save_guide_result_4)
                    )
                )
            } else if (count == 4) {
                saveResultAdapter.add(
                    saveResultAdapter.dataList.size, SaveResultData(
                        true, resources.getString(com.liningkang.ui.R.string.save_guide_result_5)
                    )
                )
            }
            sendEmptyMessageDelayed(0, animationDuration)
        }
    }

   // private var rotationAnimation: ObjectAnimator? = null


    override fun initView(savedInstanceState: Bundle?) {


        itemList.add(
            SaveResultData(
                true, resources.getString(com.liningkang.ui.R.string.save_guide_result_1)
            )
        )
        recyclerView.itemAnimator = SlideInUpAnimator()
        recyclerView.itemAnimator?.apply {
            addDuration = 100
            removeDuration = 100
            moveDuration = 500
            changeDuration = 500
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        saveResultAdapter = SaveResultAdapter(this)
        // val slideInBottomAnimationAdapter = SlideInBottomAnimationAdapter(saveResultAdapter)
        saveResultAdapter.dataList = itemList
        recyclerView.adapter = saveResultAdapter
        loadHandler.sendEmptyMessageDelayed(0, animationDuration)

   /*     rotationAnimation = ObjectAnimator.ofFloat(iconView, View.ROTATION, 0f, 360f)
        rotationAnimation?.duration = animationDuration // 旋转动画的持续时间，单位毫秒
        rotationAnimation?.repeatCount = 5 // 无限循环
        rotationAnimation?.interpolator = LinearInterpolator()
        rotationAnimation?.start()*/

        job = lifecycleScope.launch(Dispatchers.IO) {
            val updateInterval = 100L // 更新间隔时间，单位是毫秒
            val startTime = System.currentTimeMillis()
            while (System.currentTimeMillis() - startTime <= outTime) {
                val currentTime = System.currentTimeMillis() - startTime
                val progress = currentTime * 100 / outTime
                withContext(Dispatchers.Main) {
                    progressText.text = "$progress%"
                }
                delay(updateInterval)
            }
            withContext(Dispatchers.Main) {
                progressText.text = "100%"
            }
        }


    }

    override fun finish() {
        super.finish()
        job?.cancel()
     //   rotationAnimation?.removeAllListeners()
     //   iconView.clearAnimation()
        loadHandler.removeCallbacksAndMessages(null)
        // 取消页面上所有 View 的动画
        cancelAllAnimations(window.decorView)
       // rotationAnimation?.cancel()

    }

    override fun onDestroy() {
        super.onDestroy()


    }

    private var firstClickBack = 0L
    override fun onBackPressed() {
        val secondClickBack = System.currentTimeMillis()
        if (secondClickBack - firstClickBack > Constants.BACK_INTERVAL) {
            Toast.makeText(
                this,
                getString(com.liningkang.ui.R.string.press_exit_again),
                Toast.LENGTH_SHORT
            ).show()
            firstClickBack = secondClickBack
        } else {
            AppManager.resetOpenAppCount()
            AppManager.finishAllActivity()

        }
    }

    private fun cancelAllAnimations(view: View) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                cancelAllAnimations(view.getChildAt(i))
            }
        } else {
            view.clearAnimation()
        }
    }

}