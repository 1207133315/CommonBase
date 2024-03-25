package com.liningkang.common.helper

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.liningkang.common.listener.OnKeyboardCallback


object KeyBoardHelper {
    private var isKeyboardOpened = true
    private var keypadHeight = 0
    fun addOnKeyboardListener(rootView: View, onKeyboardCallback: OnKeyboardCallback? = null) {
        /* val r = Rect()
         rootView.getWindowVisibleDisplayFrame(r);*/
        val rootViewTreeObserver = rootView.viewTreeObserver
        rootViewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            keypadHeight = screenHeight - rect.bottom

            if (keypadHeight < screenHeight * 0.15) {
                // 键盘已经收起
                if (!isKeyboardOpened) {
                    // 执行相应操作
                    // 例如：隐藏相关 UI 或执行其他操作
                    //Toast.makeText(rootView.context, "键盘已收起", Toast.LENGTH_SHORT).show()
                    onKeyboardCallback?.onKeyboardDropListener()
                }
                isKeyboardOpened = true
            } else {
                if (isKeyboardOpened) {
                    // 键盘已弹出
                    onKeyboardCallback?.onKeyboardPopupListener()
                }
                isKeyboardOpened = false
            }
        }
    }


    fun showKeyBoard(editText: EditText) {
        editText.post {
            // 获取焦点并弹出键盘
            editText.requestFocus()
            editText.setSelection(editText.text.length); // 将光标移到文本末尾
            val imm =
                editText.context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm != null && imm.isActive) {
            imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
        }
    }

    fun showKeyboard(context: Context, view: View) {
        view.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}