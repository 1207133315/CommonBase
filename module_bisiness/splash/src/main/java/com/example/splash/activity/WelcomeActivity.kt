package com.example.splash.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.splash.R
import com.example.splash.dialog.AgreementDialog
import com.liningkang.base.AppManager
import com.liningkang.base.BaseCommonActivity
import com.liningkang.common.Constants
import com.liningkang.common.IntentKeys
import com.liningkang.common.RouteConfig
import com.liningkang.utils.LogUtils
import kotlinx.android.synthetic.main.activity_welcome.nextText


@Route(path = RouteConfig.ROUTER_ACTIVITY_WELCOME)
class WelcomeActivity : BaseCommonActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_welcome
    }

    override fun initView(savedInstanceState: Bundle?) {
        nextText.setOnClickListener(this)

    }






    private var firstClickBack=0L
    override fun onBackPressed() {
        val secondClickBack = System.currentTimeMillis()
         if (secondClickBack - firstClickBack > Constants.BACK_INTERVAL) {
            Toast.makeText(this, getString(com.liningkang.ui.R.string.press_exit_again), Toast.LENGTH_SHORT).show()
            firstClickBack  =secondClickBack
        } else {
             AppManager.resetOpenAppCount()
             AppManager.finishAllActivity()

        }
    }
    override fun onViewClick(v: View?) {
        super.onViewClick(v)
        when(v){
            nextText->{
                AgreementDialog(this, IntentKeys.LANGUAGE_FROM_GUIDE).show()

            }
        }
    }

}