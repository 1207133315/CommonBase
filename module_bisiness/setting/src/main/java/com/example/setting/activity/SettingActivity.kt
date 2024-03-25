package com.example.setting.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.setting.R
import com.example.setting.ShareUtil
import com.liningkang.base.BaseCommonActivity
import com.liningkang.common.IntentKeys
import com.liningkang.common.RouteConfig
import kotlinx.android.synthetic.main.activity_setting.aboutView
import kotlinx.android.synthetic.main.activity_setting.featuresView
import kotlinx.android.synthetic.main.activity_setting.googlePlayView
import kotlinx.android.synthetic.main.activity_setting.informationView
import kotlinx.android.synthetic.main.activity_setting.partnerLayout
import kotlinx.android.synthetic.main.activity_setting.passwordView
import kotlinx.android.synthetic.main.activity_setting.privacyView
import kotlinx.android.synthetic.main.activity_setting.shareView

@Route(path = RouteConfig.ROUTER_ACTIVITY_SETTING)
class SettingActivity : BaseCommonActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun initView(savedInstanceState: Bundle?) {
        informationView.setOnClickListener(this)
        partnerLayout.setOnClickListener(this)
        passwordView.setOnClickListener(this)
        aboutView.setOnClickListener(this)
        privacyView.setOnClickListener(this)
        shareView.setOnClickListener(this)
        googlePlayView.setOnClickListener(this)
        featuresView.setOnClickListener(this)
    }

    override fun onViewClick(v: View?) {
        super.onViewClick(v)
        when (v) {
            informationView -> {

                ARouter.getInstance().build(RouteConfig.ROUTER_ACTIVITY_INFORMATION)
                    .navigation(this)
            }

            partnerLayout -> {

                ARouter.getInstance().build(RouteConfig.ROUTER_ACTIVITY_PARTNER_LIST)
                    .withInt(IntentKeys.PARTNER_LIST_FROM,IntentKeys.PARTNER_LIST_FROM_SETTING)
                    .navigation(this)
            }

            passwordView -> {

                ARouter.getInstance().build(RouteConfig.ROUTER_ACTIVITY_PASSWORD_LOCK)
                    .withInt(IntentKeys.PASSWORD_LOCK_FROM, IntentKeys.PASSWORD_LOCK_FROM_SETTING)
                    .navigation(this)
            }

            aboutView -> {

                ARouter.getInstance().build(RouteConfig.ROUTER_ACTIVITY_PRIVACY_POLICY)
                    .withInt(IntentKeys.PRIVACY_FROM, IntentKeys.PRIVACY_FROM_SETTING)
                    .navigation(this)
            }

            privacyView -> {

                ARouter.getInstance().build(RouteConfig.ROUTER_ACTIVITY_PRIVACY_POLICY)
                    .navigation(this)
            }

            shareView -> {

                ShareUtil.share(this)
            }

            googlePlayView -> {

                ShareUtil.goGooglePlayScore(this)
            }
            featuresView->{
                ARouter.getInstance().build(RouteConfig.ROUTER_ACTIVITY_MAIN)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .withInt(IntentKeys.SPLASH_FROM,IntentKeys.SPLASH_FROM_SETTING)
                    .navigation(this)
                finish()
            }
        }
    }
}