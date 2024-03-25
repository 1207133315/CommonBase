package com.example.userinfo

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.liningkang.common.bean.UserInfo
import com.liningkang.utils.AESUtils


class Converters {




    @TypeConverter
    fun aesEncryptFile(userInfo: UserInfo): String {
        return AESUtils.encrypt(Gson().toJson(userInfo))
    }

    @TypeConverter
    fun aesDecryptFile(string: String): UserInfo {
        return Gson().fromJson<UserInfo>(AESUtils.decrypt(string), UserInfo::class.java)
    }
/*    @TypeConverter
    fun toRecordList(recordList: String): MutableList<RecordData> {
        return Gson().fromJson<MutableList<RecordData>>(recordList,
            object :TypeToken <MutableList<RecordData>>(){}.type
        )
    }
    @TypeConverter
    fun fromRecordList(recordList: MutableList<RecordData>): String {
        return recordList.toJson()
    }*/



}