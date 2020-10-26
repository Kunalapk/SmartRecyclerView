package com.kunalapk.smartrecyclerview.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

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

    fun getIntent(context: Context,activity: Class<*>,queryString:String?):Intent{
        val intent = Intent(context,activity)
        if(!queryString.isNullOrEmpty()){
            addBundleToIntent(intent,queryString)
        }
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