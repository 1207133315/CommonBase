package com.liningkang.utils

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar


object ToastUtils {
    private var context: Context? = null
    fun init(context: Context) {
        this.context = context
    }

    fun showToast(message: String,layout: View? = null, duration: Int = Toast.LENGTH_SHORT) {


        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (layout == null)
                return
            val snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT)
            val view = snackbar.view
            view.setBackgroundResource(R.drawable.custom_toast_bg)
            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).textSize =
               DeviceUtils.dp2px(context!!,12f)
            snackbar.show()

        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.custom_toast, null)
            val toast = Toast(context)
            toast.duration = duration
             toast.setGravity(Gravity.CENTER , 0, 0)
            toast.view = view

            val toastTextView: TextView = view.findViewById(R.id.toast_text)
            toastTextView.text = message

            toast.show()
        }

    }
}