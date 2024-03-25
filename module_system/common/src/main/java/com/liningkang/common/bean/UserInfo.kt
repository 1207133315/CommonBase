package com.liningkang.common.bean

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.liningkang.common.BR


/**
 * @Author ；Ningkang.Li
 * @Time ：2023/5/22日 15点
 * @Description ；---
 */
@Entity
class UserInfo : BaseObservable() {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var userId: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.userId)
        }





}



