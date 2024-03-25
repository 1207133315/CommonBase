package com.liningkang.network

import com.google.gson.annotations.SerializedName

class DataResponse<T>(
    @SerializedName("code")
    var code: Int,
    @SerializedName("status")
    var status: Int,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("data")
    var data: T?
) {

    fun isSuccess(): Boolean {
        return status == NetworkConfig.HTTP_OK
    }
}