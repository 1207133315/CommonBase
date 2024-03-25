package com.liningkang.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.DialogInterface.OnDismissListener
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.liningkang.base.BaseActivity
import com.liningkang.base.BaseBindingDialog
import com.liningkang.base.BaseDialog
import com.liningkang.ui.databinding.DialogCustomBinding
import kotlinx.android.synthetic.main.dialog_custom.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CustomDialog(activity: AppCompatActivity) :
    BaseDialog(activity, R.style.CustomDialog) {
    private var dismissListener :(()->Unit)?=null
    override fun getLayoutId(): Int = R.layout.dialog_custom
    companion object{
        const val SHOW_LONG_TIME=1000*10
    }
    private var startShowTime=0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvContent.text = activity.resources.getString(R.string.loading)
        setCanceledOnTouchOutside(true)
        setCancelable(false)
        setOnDismissListener(object :OnDismissListener{
            override fun onDismiss(dialog: DialogInterface?) {
                animationView.cancelAnimation()
                dismissListener?.invoke()
            }

        })
    }

     fun setOnDismissListener(dismissListener :(()->Unit)?=null) {
       this.dismissListener=dismissListener
    }

    override fun show() {
        super.show()
        startShowTime=System.currentTimeMillis()
        lifecycleScope.launch(Dispatchers.IO){
            while (System.currentTimeMillis()-startShowTime<SHOW_LONG_TIME){
                delay(1000)
            }
            withContext(Dispatchers.Main){
                dismiss()
            }
        }
        animationView.playAnimation()
    }


}