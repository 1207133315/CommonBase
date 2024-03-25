package com.example.splash.dialog

import android.content.Intent
import android.graphics.Paint
import android.view.View
import android.view.View.OnClickListener

import com.alibaba.android.arouter.launcher.ARouter

import com.liningkang.base.BaseActivity
import com.liningkang.base.BaseBottomDialog
import com.liningkang.common.RouteConfig
import com.example.splash.R
import com.liningkang.base.AppManager
import com.liningkang.common.IntentKeys
import com.liningkang.utils.CommSharedUtil
import kotlinx.android.synthetic.main.dialog_agreement.consentButton
import kotlinx.android.synthetic.main.dialog_agreement.disagreeButton
import kotlinx.android.synthetic.main.dialog_agreement.watchNowText


class AgreementDialog(context: BaseActivity, fromType: Int) : BaseBottomDialog(context),
    OnClickListener {


    private var type = fromType
     override fun getLayoutResId(): Int {
         return R.layout.dialog_agreement
     }

     override fun initView() {
         watchNowText.paintFlags = watchNowText.paintFlags or Paint.UNDERLINE_TEXT_FLAG
         /*     hintText2.paintFlags = hintText2.paintFlags or Paint.UNDERLINE_TEXT_FLAG
              hintText4.paintFlags = hintText4.paintFlags or Paint.UNDERLINE_TEXT_FLAG
              hintText4.setOnClickListener(this)
              hintText2.setOnClickListener(this)*/
         watchNowText.setOnClickListener(this)
         disagreeButton.setOnClickListener(this)
         consentButton.setOnClickListener(this)
     }


     override fun onClick(v: View) {
        when (v) {
            /* hintText4 -> {
                 onEvent("隐私政策弹窗_隐私政策点击")
                 ARouter.getInstance().build(RouteConfig.ROUTER_ACTIVITY_PRIVACY_POLICY).navigation()
             }

             hintText2 -> {

             }*/
            watchNowText -> {


                ARouter.getInstance().build(RouteConfig.ROUTER_ACTIVITY_PRIVACY_POLICY).navigation()

            }

            disagreeButton -> {
                dismiss()
                AppManager.resetOpenAppCount()
                AppManager.finishAllActivity()

              //  AppManager.exitApp()
            }

            consentButton -> {

                // 进入下一个页面
                if (type == IntentKeys.LANGUAGE_FROM_GUIDE) {
                     ARouter.getInstance().build(RouteConfig.ROUTER_ACTIVITY_EXPLAIN)
                         .withInt(IntentKeys.INTERSTITIAL_AD_NUMBER, 1)
                         .navigation(activity)
                    dismiss()
                } else {
                    dismiss()
                }

                /* ARouter.getInstance().build(RouteConfig.ROUTER_ACTIVITY_SAVE_GUIDE_RESULT).navigation()
                 AppManager.finishAllActivity()*/
            }
        }
    }


}