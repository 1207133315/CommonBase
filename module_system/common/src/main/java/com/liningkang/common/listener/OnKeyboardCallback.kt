package com.liningkang.common.listener

interface OnKeyboardCallback {
    /*
    * 键盘收起回调
    * */
    fun onKeyboardDropListener()

    /*
    * 键盘弹出
    * */
    fun onKeyboardPopupListener()
}