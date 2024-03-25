package com.hnquxing.home_main.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.liningkang.base.DatabindingViewHolder
import com.liningkang.base.FastClick


/*
*  RecyclerViewAdapter+DataBinding的基类,仅限于配合DataBinding使用的Adapter
*  T : 与item绑定的dataBinding
*  E : 列表数据类型
* */


abstract class BaseDatabindingRecyclerAdapter<T : ViewDataBinding, E>(context: Context) :
    RecyclerView.Adapter<DatabindingViewHolder<T>>() {
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
    fun add(position: Int,data:E){
        dataList.add(position,data)
        notifyItemInserted(position)
    }

    open override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DatabindingViewHolder<T> {
        var dataBinding: T = DataBindingUtil.inflate<T>(
            LayoutInflater.from(context),
            getItemLayoutId(parent,viewType),
            parent,
            false
        )
        return DatabindingViewHolder(dataBinding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: DatabindingViewHolder<T>, position: Int) {
        onBind(holder, position)

        getClickView(holder).tag = dataList[position%dataList.size]
        if (mOnItemClickListener!=null){
            getClickView(holder).setOnClickListener {
                val data = it.tag as E
                if (data != currentData) {
                    mOnItemClickListener?.invoke(data, position)
                    currentData = data
                }

            }
        }
        holder.binding.executePendingBindings()// 解决刷新列表时数据跳动问题

    }

    //默认是条目的点击  如果想更改点击的对象  重写此方法即可
    open fun getClickView(holder: DatabindingViewHolder<T>): View {
        return holder.itemView
    }

    open fun getItemData(position: Int): E {
        return dataList[position%dataList.size]
    }

    abstract fun getItemLayoutId( parent: ViewGroup,
                                  viewType: Int): Int

    abstract fun onBind(holder: DatabindingViewHolder<T>, position: Int)

}