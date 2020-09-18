package com.kunalapk.smartrecyclerview.helper

import android.app.Activity
import android.content.Context
import android.content.Intent

object IntentHelper {
    private var TAG = javaClass.simpleName

    fun getIntent(context: Context,activityName: String,queryString:String?):Intent{
        val intent = Intent()
        if(!queryString.isNullOrEmpty()){
            addBundleToIntent(intent,queryString)
        }
        intent.setClassName(context,activityName)
        return intent
    }

    fun addBundleToIntent(intent: Intent,queryLong:String){
        val array = queryLong.split("&")
        array.forEach {
            val query = it.split("=")
            intent.putExtra(query[0],query[1])
        }
    }
}