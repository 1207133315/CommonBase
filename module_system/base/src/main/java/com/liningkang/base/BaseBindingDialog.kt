package com.liningkang.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingDialog<VDB : ViewDataBinding>(activity: BaseActivity , attrId: Int) : BaseDialog(activity, attrId) {
    constructor(activity: BaseActivity) : this(activity, R.style.fullScreenDialog)
    var mBinding:VDB
    init {
        mBinding = DataBindingUtil.inflate<VDB>(
            LayoutInflater.from(activity),
            getLayoutId(),
            null,
            false
        )
        mBinding.lifecycleOwner=this
        setContentView(mBinding.root)
        initView(mBinding)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    abstract fun initView(binding:VDB)
    abstract fun onClick(view:View)
}