package com.hnquxing.home_main.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.liningkang.base.BaseViewHolder
import com.liningkang.base.DatabindingViewHolder
import com.liningkang.base.FastClick


/*
*  RecyclerViewAdapter+DataBinding的基类,仅限于配合DataBinding使用的Adapter
*  T : 与item绑定的dataBinding
*  E : 列表数据类型
* */

abstract class BaseRecyclerAdapter<E>(context: Context) :
    RecyclerView.Adapter<BaseViewHolder>() {
    var context: Context = context
    private var currentData: E? = null
    var dataList: MutableList<E> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
        }
    private var mOnItemClickListener: ((data: E?, position: Int) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener: ((data: E?, position: Int) -> Unit)?) {
        mOnItemClickListener = onItemClickListener
        notifyDataSetChanged()
    }

    fun add(position: Int, data: E) {
        dataList.add(position, data)
        notifyItemInserted(position)
    }

    open override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {

        return BaseViewHolder(
            LayoutInflater.from(context).inflate(getItemLayoutId(parent,viewType), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder:BaseViewHolder, position: Int) {
        if (dataList.size?:0==0){
            return
        }
        onBind(holder, position)

        getClickView(holder).tag = dataList[position%dataList.size]
        if (mOnItemClickListener != null) {
            getClickView(holder).setOnClickListener {
                val data = it.tag as E
               // if (data != currentData) {
                    mOnItemClickListener?.invoke(data, position%dataList.size)
                    currentData = data
               // }

            }
        }

    }

    //默认是条目的点击  如果想更改点击的对象  重写此方法即可
    open fun getClickView(holder: BaseViewHolder): View {
        return holder.itemView
    }

    open fun getItemData(position: Int): E {
        return dataList[position%dataList.size]
    }

    abstract fun getItemLayoutId( parent: ViewGroup?=null,
                                  viewType: Int=0): Int

    abstract fun onBind(holder: BaseViewHolder, position: Int)

}