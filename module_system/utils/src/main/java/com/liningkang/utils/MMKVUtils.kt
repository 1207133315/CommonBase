package com.liningkang.utils

import com.tencent.mmkv.MMKV


object MMKVUtils {
    private var mmkv = MMKV.defaultMMKV()


    // 存数据
    fun encode(key: String?, value: Any?) {
        when (value) {
            is String -> {
                mmkv.encode(key, value)
            }
            is Int -> {
                mmkv.encode(key, value)
            }
            is Boolean -> {
                mmkv.encode(key, value)
            }
            is Float -> {
                mmkv.encode(key, value)
            }
            is Long -> {
                mmkv.encode(key, value)
            }
            is Set<*> -> {
                mmkv.encode(key, value as Set<String>)
            }
            null -> {
                mmkv.removeValueForKey(key)
            }
        }
    }


    // 取数据
    fun <T> decode(
        key: String,
        defaultObject: T?
    ): T? {
        val any = if (defaultObject is String) {
            mmkv.decodeString(key, defaultObject)
        } else if (defaultObject is Int) {
            mmkv.decodeInt(key, defaultObject)
        } else if (defaultObject is Boolean) {
            mmkv.decodeBool(key, defaultObject)
        } else if (defaultObject is Float) {
            mmkv.decodeFloat(key, defaultObject)
        } else if (defaultObject is Long) {
            mmkv.decodeLong(key, defaultObject)
        } else if (defaultObject is Set<*>) {
            mmkv.decodeStringSet(key, defaultObject as Set<String>)
        } else {
            decodeString(key)
        }
        return any as T
    }


    fun decodeString(key: String?): String? {
        return decodeString(key, null)
    }

    fun decodeString(key: String?, defaultObject: String?): String? {
        return if (defaultObject.isNullOrEmpty()) {
            mmkv.decodeString(key)
        } else {
            mmkv.decodeString(key, defaultObject)
        }
    }

    fun decodeInt(key: String?): Int? {
        return decodeInt(key, null)
    }

    fun decodeInt(key: String?, defaultObject: Int?): Int? {
        return if (defaultObject == null) {
            mmkv.decodeInt(key)
        } else {
            mmkv.decodeInt(key, defaultObject)
        }
    }

    fun decodeBool(key: String?): Boolean? {
        return decodeBool(key, null)
    }

    fun decodeBool(key: String?, defaultObject: Boolean?): Boolean? {
        return if (defaultObject == null) {
            mmkv.decodeBool(key)
        } else {
            mmkv.decodeBool(key, defaultObject)
        }
    }

    fun decodeFloat(key: String?): Float? {
        return decodeFloat(key, null)
    }

    fun decodeFloat(key: String?, defaultObject: Float?): Float? {
        return if (defaultObject == null) {
            mmkv.decodeFloat(key)
        } else {
            mmkv.decodeFloat(key, defaultObject)
        }
    }

    fun decodeDouble(key: String?): Double? {
        return decodeDouble(key, null)
    }

    fun decodeDouble(key: String?, defaultObject: Double?): Double? {
        return if (defaultObject == null) {
            mmkv.decodeDouble(key)
        } else {
            mmkv.decodeDouble(key, defaultObject)
        }
    }

    fun decodeLong(key: String?): Long? {
        return decodeLong(key, null)
    }

    fun decodeLong(key: String?, defaultObject: Long?): Long? {
        return if (defaultObject == null) {
            mmkv.decodeLong(key)
        } else {
            mmkv.decodeLong(key, defaultObject)
        }
    }

    fun decodeBytes(key: String?): ByteArray? {
        return decodeBytes(key, null)
    }

    fun decodeBytes(key: String?, defaultObject: ByteArray?): ByteArray? {
        return if (defaultObject == null) {
            mmkv.decodeBytes(key)
        } else {
            mmkv.decodeBytes(key, defaultObject)
        }
    }
}