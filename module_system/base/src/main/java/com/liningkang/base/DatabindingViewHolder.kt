package com.liningkang.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class DatabindingViewHolder<T:ViewDataBinding>(binding: T) : RecyclerView.ViewHolder(binding.root) {
    var binding=binding

}
