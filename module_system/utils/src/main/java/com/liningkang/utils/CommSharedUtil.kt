package com.liningkang.utils

import android.content.Context
import android.content.SharedPreferences

object CommSharedUtil {
    private const val SHARED_PATH = "water_app_info"
    const val KEY_BIG_NOTIFICATION_IS_OPEN = "key_big_notification_is_open"
    const val KEY_IS_SET_LANGUAGE = "key_is_set_language"
    const val KEY_IS_OPEN_LOCK = "key_is_open_lock"
    const val KEY_USER_PASSWORD = "key_user_password"
    const val KEY_IS_NEW_USER = "key_is_new_user"
    const val RECORD_COUNT = "record_count"
    const val KEY_LAST_TRAINING_TIME = "key_last_training_time"
    const val KEY_GROUP_ONE_COUNT = "KEY_GROUP_ONE_COUNT"
    const val KEY_GROUP_TWO_COUNT = "KEY_GROUP_TWO_COUNT"
    const val KEY_GROUP_THREE_COUNT = "KEY_GROUP_THREE_COUNT"
    const val KEY_GROUP_FOUR_COUNT = "KEY_GROUP_FOUR_COUNT"
    const val KEY_LAST_RECORD_TIME = "KEY_LAST_RECORD_TIME"
    const val KEY_VIDEO_LOCK_MAP = "KEY_VIDEO_LOCK"
    const val IS_FIRST_CLICK_ANALYSIS = "is_first_click_analysis"
    const val IS_FIRST_CLICK_BOX_BREATHING = "is_first_click_box_breathing"
    const val IS_FIRST_CLICK_BREATH_HOLDING = "is_first_click_breath_holding"
    const val IS_FIRST_CLICK_MINDFUL = "is_first_click_mindful"
    const val IS_FIRST_CLICK_KEGEL_EXERCISES = "is_first_click_kegel_exercises"
    const val IS_FIRST_CLICK_ADVICE = "is_first_click_advice"
    const val KEY_APP_OPEN_TIME = "key_app_open_time"
    private var sharedPreferences: SharedPreferences? = null


    fun initialize(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE)
        }
    }


    fun putInt(key: String?, value: Int) {
        sharedPreferences?.edit()?.apply {
            putInt(key, value)
            apply()
        }
    }

    fun putLong(key: String?, value: Long) {
        sharedPreferences?.edit()?.apply {
            putLong(key, value)
            apply()
        }
    }

    fun getInt(key: String?): Int {

        return sharedPreferences?.getInt(key, 0) ?: 0
    }

    fun getLong(key: String?): Long {
        return getLong(key, 0L)
    }

    fun getLong(key: String?, defValue: Long): Long {

        return sharedPreferences?.getLong(key, defValue) ?: 0L
    }

    fun putString(key: String?, value: String?) {
        sharedPreferences?.edit()?.apply {
            putString(key, value)
            apply()
        }

    }


    fun getString(key: String?): String? {

        return sharedPreferences?.getString(key, null)
    }


    fun putBoolean(key: String?, value: Boolean) {
        sharedPreferences?.edit()?.apply {
            putBoolean(key, value)
            apply()
        }
    }


    fun getBoolean(key: String?, defValue: Boolean): Boolean {

        return sharedPreferences?.getBoolean(key, defValue) ?: false
    }

    fun getInt(key: String?, defValue: Int): Int {

        return sharedPreferences?.getInt(key, defValue) ?: 0
    }

    fun remove(key: String?) {
        sharedPreferences?.edit()?.apply {
            remove(key)
            apply()
        }
    }
}