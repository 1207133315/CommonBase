package com.example.splash.adapter

import android.content.Context
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.splash.R
import com.example.splash.data.LanguageData
import com.example.splash.databinding.LanguageItemBinding
import com.hnquxing.home_main.common.BaseDatabindingRecyclerAdapter
import com.liningkang.base.DatabindingViewHolder

/**
 * @Author ；Ningkang.Li
 * @Time ：2023/5/24日 13点
 * @Description ；---
 */
class LanguageAdapter(context: Context) :
    BaseDatabindingRecyclerAdapter<LanguageItemBinding, LanguageData>(context) {
    override fun getItemLayoutId(parent: ViewGroup,
                                 viewType: Int): Int {
        return R.layout.language_item
    }

    override fun onBind(holder: DatabindingViewHolder<LanguageItemBinding>, position: Int) {
        val binding = holder.binding
        val itemData = getItemData(position)
        binding.languageData = itemData
        if (itemData.isChecked) {
            Glide.with(context).load(R.drawable.language_checked).into(binding.checkBox)
        } else {
            Glide.with(context).load(R.drawable.language_unchecked).into(binding.checkBox)
        }

        Glide.with(context).load(itemData.iconRes).into(binding.nationalFlagImage)
        /*binding.checkBox.setOnClickListener {
            dataList.forEach {data->
                data.isChecked = itemData ==  data
            }
         notifyDataSetChanged()
        }*/

    }
}