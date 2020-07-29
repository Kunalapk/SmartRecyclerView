package com.kunalapk.smartrecyclerview.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.kunalapk.smartrecyclerview.utils.NotificationSharedPrefConstant

internal class SharedPreferencesManager private constructor(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(NotificationSharedPrefConstant.PREF_LOGIN_CREDENTIAL, Context.MODE_PRIVATE)

    companion object {
        private var singleton: SharedPreferencesManager? = null

        fun with(context: Context?): SharedPreferencesManager? {
            if (singleton == null) {
                synchronized(lock = SharedPreferencesManager::class.java) {
                    if (singleton == null)
                        singleton = Builder(context = context).build()
                }
            }

            return singleton
        }
    }


    val all: Map<String, *>
        get() = preferences.all


    fun getString(key: String, defValue: String?): String? = preferences.getString(key, defValue)

    fun getStringSet(key: String, defValues: Set<String>): Set<String>? =
            preferences.getStringSet(key, defValues)


    fun getInt(key: String, defValue: Int): Int = preferences.getInt(key, defValue)

    fun getLong(key: String, defValue: Long): Long = preferences.getLong(key, defValue)


    fun getFloat(key: String, defValue: Float): Float = preferences.getFloat(key, defValue)


    fun getBoolean(key: String, defValue: Boolean): Boolean = preferences.getBoolean(key, defValue)


    operator fun contains(key: String): Boolean = preferences.contains(key)

    @SuppressLint("CommitPrefEdits")
    fun edit(): SharedPreferences.Editor = preferences.edit()

    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) =
            preferences.registerOnSharedPreferenceChangeListener(listener)

    fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) =
            preferences.unregisterOnSharedPreferenceChangeListener(listener)


    private class Builder(context: Context?) {
        private val context: Context

        init {
            if (context == null)
                throw IllegalArgumentException("Context must not be null.")

            this.context = context.applicationContext
        }


        fun build(): SharedPreferencesManager = SharedPreferencesManager(context = context)
    }
}