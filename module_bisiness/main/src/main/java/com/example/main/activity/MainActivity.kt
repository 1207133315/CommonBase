package com.example.main.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.Window
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.main.BuildConfig
import com.example.main.R
import com.example.main.databinding.ActivityMainBinding


import com.liningkang.base.AppManager


import com.liningkang.base.BaseCommonActivity
import com.liningkang.base.BaseFragment
import com.liningkang.base.BaseViewModelDataBindingActivity
import com.liningkang.common.Constants
import com.liningkang.common.IntentKeys
import com.liningkang.common.RouteConfig
import com.liningkang.common.listener.OnDismissListener
import com.liningkang.utils.CommSharedUtil
import com.liningkang.utils.LogUtils
import com.liningkang.utils.fromJson

import com.example.main.viewmodel.MainViewModel
import com.liningkang.common.bean.NotificationData

@Route(path = RouteConfig.ROUTER_ACTIVITY_MAIN)
class MainActivity : BaseViewModelDataBindingActivity<MainViewModel, ActivityMainBinding>() {
    companion object {
        var context: MainActivity? = null
    }

    private var currentFragment: BaseFragment? = null
    private var savedInstanceState: Bundle? = null
    private var recordFragment: BaseFragment? = null
    private var reportFragment: BaseFragment? = null
    private var trainFragment: BaseFragment? = null
    private var achieveFragment: BaseFragment? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        this.savedInstanceState = outState
    }

    override fun initView(savedInstanceState: Bundle?) {
        context = this
        this.savedInstanceState = savedInstanceState
        initFragment()
        binding.mainActivity = this
        binding.mainViewMode = viewModel
        AppManager.init(this)

        val isNewUser = CommSharedUtil.getBoolean(CommSharedUtil.KEY_IS_NEW_USER, true)

    }



    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)


    }




    fun notificationNext(notificationData: NotificationData?){

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
            AppManager.minimizeApp(this)
        }
    }


    override fun onViewClick(v: View?) {
        super.onViewClick(v)
        when (v) {

        }
    }

    private fun initFragment() {
        recordFragment = ARouter.getInstance().build(RouteConfig.ROUTER_FRAGMENT_RECORD)
            .navigation() as BaseFragment?
        reportFragment = ARouter.getInstance().build(RouteConfig.ROUTER_FRAGMENT_REPORT)
            .navigation() as BaseFragment?
        trainFragment = ARouter.getInstance().build(RouteConfig.ROUTER_FRAGMENT_TRAIN)
            .navigation() as BaseFragment?
        achieveFragment = ARouter.getInstance().build(RouteConfig.ROUTER_FRAGMENT_ACHIEVE)
            .navigation() as BaseFragment?

        //   addFragment(reportFragment, reportFragment?.javaClass?.simpleName)


    }


    @Synchronized
    private fun addFragment(to: BaseFragment?, tag: String?) {

        try {
            if (to != null) {
                var fragment: Fragment? = null
                if (savedInstanceState != null) {
                    fragment = supportFragmentManager?.getFragment(savedInstanceState!!, tag ?: "")
                }

                if (currentFragment == null) {
                    if (null == supportFragmentManager.findFragmentByTag(tag)) {
                        if (!to.isAdded) {
                            supportFragmentManager.beginTransaction()
                                .remove(to)
                                .add(R.id.frameLayout, to, tag)
                                .commitAllowingStateLoss()
                        }
                    }
                } else {
                    if (!to.isAdded) {
                        LogUtils.d("addFragment", "未添加")
                        if (null == supportFragmentManager.findFragmentByTag(tag)) {
                            supportFragmentManager.beginTransaction()
                                .hide(currentFragment!!)
                                .remove(to)
                                .add(R.id.frameLayout, to, tag)
                                .commitAllowingStateLoss()
                        }
                    } else {
                        LogUtils.d("addFragment", "已添加")
                        supportFragmentManager.beginTransaction()
                            .hide(currentFragment!!)
                            .show(to)
                            .commitAllowingStateLoss()
                    }
                }
                currentFragment = to
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }



}