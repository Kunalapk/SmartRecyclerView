package com.kunalapk.smartrecyclerview.service

import android.app.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kunalapk.smartrecyclerview.helper.NotificationHelper
import org.json.JSONObject
import java.net.URLDecoder
import java.net.URLEncoder

class MessagingService : FirebaseMessagingService() {

    private lateinit var mNotificationHelper: NotificationHelper

    companion object {
        private val TAG = MessagingService::class.java.simpleName
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val dataObject = JSONObject(remoteMessage.data.toString())
        var title:String? = null
        var message:String? = null
        var image:String? = null
        var queryString:String? = null
        var activityName:String? = null
        var code:Int = 999

        if(dataObject.has("title") && dataObject.has("message")){
            title = dataObject.getString("title")
            message = dataObject.getString("message")
        }else{
            return
        }

        if(dataObject.has("image")){
            image = dataObject.getString("image")
        }

        if(dataObject.has("code")){
            code = dataObject.getInt("code")
        }

        if(dataObject.has("query")){
            queryString = URLDecoder.decode(dataObject.getString("query"),"UTF-8")
        }

        if(dataObject.has("activity")){
            activityName = dataObject.getString("activity")
        }

        if(!activityName.isNullOrEmpty()){
            try {
                val intent = Intent()
                if(!queryString.isNullOrEmpty()){
                    addBundleToIntent(intent,queryString)
                }
                intent.setClassName(this,activityName)
                startActivity(intent)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                loadLargeIconAndNotification(intent,code,title,message,image)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }


    }

    private fun addBundleToIntent(intent: Intent,queryLong:String){
        val array = queryLong.split("&")
        array.forEach {
            val query = it.split("=")
            intent.putExtra(query[0],query[1])
        }
    }


    private fun loadLargeIconAndNotification(intent:Intent,code:Int,title:String,message:String,url:String?){
        val finish_target = object : CustomTarget<Bitmap>() {

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                makeNotification(intent,code,title,message,resource)
            }

            override fun onLoadStarted(placeholder: Drawable?) {
                super.onLoadStarted(placeholder)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                makeNotification(intent,code,title,message,null)

            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }
        }

        Glide.with(this)
            .asBitmap()
            .load(url).into(finish_target)
    }


    private fun makeNotification(intent:Intent,code:Int,title:String,message:String,icon:Bitmap?){
        val pendingIntent:PendingIntent = PendingIntent.getActivity(this,code,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        mNotificationHelper = NotificationHelper(this)

        if(icon!=null){
            mNotificationHelper.notify(code,mNotificationHelper.getNotification2(title,message,pendingIntent,icon))
        }else{
            mNotificationHelper.notify(code,mNotificationHelper.getNotification2(title,message,pendingIntent))
        }
    }

}