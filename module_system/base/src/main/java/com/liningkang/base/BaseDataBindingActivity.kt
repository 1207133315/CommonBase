package com.liningkang.base

import android.app.Dialog
import android.os.Bundle
import android.os.PersistableBundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.ParameterizedType

/**
 * @description ViewModel+DataBinding的Activity基类
 */
abstract class BaseDataBindingActivity< VDB : ViewDataBinding> :
    BaseActivity() {
    var mLoadDialog // 加载框
            : Dialog? = null



    lateinit var binding: VDB

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //打印堆栈ID
//        LogUtils.e("getTaskId = $taskId")

        binding = DataBindingUtil.setContentView(this, getLayoutId())
        //所有布局中dababinding对象变量名称都是vm
        binding?.executePendingBindings() //立即更新UI

        initView(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }

    }

    /**
     * @description 收集流,目的时简化flow调用collect收集时的代码
     * @param action-收集流时要执行的代码块
     * @return job-当前协程(lifecycleScope)
     */
    fun <T> Flow<T>.collectIn(
        action: suspend (T) -> Unit
    ): Job = lifecycleScope.launch {
        collect(action)
    }




    /**
     * 得到图片的路径
     *
     * @param fileName
     * @param requestCode
     * @param data
     * @return
     */
//    fun getFilePath(fileName: String?, requestCode: Int, data: Intent): String? {
//        if (requestCode == CAMERA) {
//            return fileName
//        } else if (requestCode == PHOTO) {
//            val uri: Uri? = data.data
//            return FileUtils.getFilePathByUri(this, uri)
//        }
//        return null
//    }
}