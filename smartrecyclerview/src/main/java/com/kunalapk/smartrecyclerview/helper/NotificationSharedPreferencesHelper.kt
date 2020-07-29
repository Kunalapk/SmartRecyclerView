package com.kunalapk.smartrecyclerview.helper

import android.content.Context
import com.kunalapk.smartrecyclerview.manager.SharedPreferencesManager
import com.kunalapk.smartrecyclerview.utils.NotificationSharedPrefConstant

object NotificationSharedPreferencesHelper {
    private var TAG = javaClass.simpleName

    fun getProfileName(context: Context): String? = SharedPreferencesManager.with(context = context)!!.getString(NotificationSharedPrefConstant.STORE_PROFILE_NAME, null)

    fun storeProfileName(context: Context,installId: String) {
        SharedPreferencesManager.with(context)!!.edit().putString(NotificationSharedPrefConstant.STORE_PROFILE_NAME, installId).apply()
    }

    fun getProfileLastName(context: Context): String? = SharedPreferencesManager.with(context = context)!!.getString(NotificationSharedPrefConstant.STORE_PROFILE_LAST_NAME, null)

    fun storeProfileLastName(context: Context,installId: String) {
        SharedPreferencesManager.with(context)!!.edit().putString(NotificationSharedPrefConstant.STORE_PROFILE_LAST_NAME, installId).apply()
    }

    fun getProfileFullName(context: Context): String? = SharedPreferencesManager.with(context = context)!!.getString(NotificationSharedPrefConstant.STORE_PROFILE_FULL_NAME, null)

    fun storeProfileFullName(context: Context,installId: String) {
        SharedPreferencesManager.with(context)!!.edit().putString(NotificationSharedPrefConstant.STORE_PROFILE_FULL_NAME, installId).apply()
    }

}