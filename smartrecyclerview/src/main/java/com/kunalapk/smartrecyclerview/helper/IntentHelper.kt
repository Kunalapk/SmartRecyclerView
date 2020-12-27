package com.kunalapk.smartrecyclerview.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.kunalapk.smartrecyclerview.utils.SmartRecyclerViewConstants
import java.util.*

object IntentHelper {
    private var TAG = javaClass.simpleName

    fun getIntent(context: Context,activityName: String,queryString:String?,uuid:String?,campaign_name:String?):Intent{
        val intent = Intent()
        if(!queryString.isNullOrEmpty()){
            addBundleToIntent(intent,queryString)
        }
        addUUIDToIntent(uuid,campaign_name,intent)
        intent.setClassName(context,activityName)
        return intent
    }

    fun getIntent(context: Context,activity: Class<*>,queryString:String?,uuid:String?,campaign_name:String?):Intent{
        val intent = Intent(context,activity)
        if(!queryString.isNullOrEmpty()){
            addBundleToIntent(intent,queryString)
        }
        addUUIDToIntent(uuid,campaign_name,intent)
        return intent
    }

    private fun addUUIDToIntent(uuid:String?,campaign_name:String?,intent: Intent){
        if(!uuid.isNullOrEmpty() && !campaign_name.isNullOrEmpty()){
            intent.putExtra(SmartRecyclerViewConstants.KEY_NOTIFICATION_REPORT_UUID,uuid)
            intent.putExtra(SmartRecyclerViewConstants.KEY_NOTIFICATION_REPORT_CAMPAIGN_NAME,campaign_name)
        }
    }

    fun addBundleToIntent(intent: Intent,queryLong:String){
        val array = queryLong.split("&")
        array.forEach {
            val query = it.split("=")
            intent.putExtra(query[0],query[1])
        }
    }
}