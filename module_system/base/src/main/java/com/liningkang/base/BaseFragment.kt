package com.liningkang.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter

abstract open class BaseFragment : Fragment(), OnClickListener {

    companion object {
        fun getInstance(path: String): BaseFragment? {
            return ARouter.getInstance().build(path).navigation() as? BaseFragment
        }
    }

    protected lateinit var mActivity: Activity
    var label: String = ""
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
        label = this.javaClass.simpleName
    }


    open fun next() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    final override fun onClick(v: View?) {
        if (!FastClick.isFastClick()) {
            onViewClick(v)
        }
    }

    open  fun onViewClick(v: View?) {

    }

    /*    abstract fun provideViewModelFactory(): ViewModelProvider.Factory

        inline fun <reified T : BaseViewModel<*>> BaseFragment.viewModel(): Lazy<T> {
            return viewModels<T> { provideViewModelFactory() }
        }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }



    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initView(view: View, savedInstanceState: Bundle?)
}