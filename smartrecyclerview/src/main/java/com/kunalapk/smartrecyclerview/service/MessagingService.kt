package com.kunalapk.smartrecyclerview.service

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.BuildConfig
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kunalapk.smartrecyclerview.helper.IntentHelper
import com.kunalapk.smartrecyclerview.helper.NotificationHelper
import com.kunalapk.smartrecyclerview.helper.NotificationSharedPreferencesHelper
import org.json.JSONObject
import java.lang.Exception

open class MessagingService : FirebaseMessagingService() {

    private lateinit var mNotificationHelper: NotificationHelper

    companion object {
        private val TAG = MessagingService::class.java.simpleName
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        try {
            val dataObject = JSONObject(remoteMessage.data as Map<String?, String?>)
            var title: String = ""
            var message: String = ""
            var image: String? = null
            var queryString: String? = null
            var activityName: String? = null
            var url: String? = null
            var code: Int = 999
            var isProfileIcon: Boolean = false
            val profileFirstName: String? = NotificationSharedPreferencesHelper.getProfileName(baseContext)
            val profileLastName: String? = NotificationSharedPreferencesHelper.getProfileLastName(baseContext)
            val profileFullName: String? = NotificationSharedPreferencesHelper.getProfileFullName(baseContext)

            if(dataObject.has("title") && dataObject.has("message")){
                title = dataObject.getString("title")
                message = dataObject.getString("message")

                if(profileFirstName!=null){
                    title = title.replace("%firstname%",profileFirstName)
                    message = message.replace("%firstname%",profileFirstName)
                }

                if(profileLastName!=null){
                    title = title.replace("%lastname%",profileLastName)
                    message = message.replace("%lastname%",profileLastName)
                }

                if(profileFullName!=null){
                    title = title.replace("%fullname%",profileFullName)
                    message = message.replace("%fullname%",profileFullName)
                }
            }else{
                return
            }

            if(dataObject.has("image")){
                image = dataObject.getString("image")
            }

            if(dataObject.has("is_profile_icon")){
                isProfileIcon = dataObject.getBoolean("is_profile_icon")
            }

            if(dataObject.has("code")){
                code = dataObject.getInt("code")
            }

            if(dataObject.has("query")){
                queryString = dataObject.getString("query")
            }

            if(dataObject.has("activity")){
                activityName = dataObject.getString("activity")
            }

            if(dataObject.has("url")){
                url = dataObject.getString("url")
            }


            if(!activityName.isNullOrEmpty()) {
                prepareNotification(title,message,activityName,queryString,code,image,isProfileIcon)
            }else if(!url.isNullOrEmpty()) {
                prepareNotification(title,message,url,code,image,isProfileIcon)
            }
        }catch (e:Exception){

        }
    }

    fun prepareNotification(title: String,message: String,activity:Class<*>,queryString:String?,code: Int,image:String?,isProfileIcon: Boolean){

        try {
            val intent = IntentHelper.getIntent(this,activity,queryString)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK)
            loadLargeIconAndNotification(intent,code,title,message,image,isProfileIcon)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    fun prepareNotification(title: String,message: String,activityName:String,queryString:String?,code: Int,image:String?,isProfileIcon: Boolean){

        try {
            val intent = IntentHelper.getIntent(this,activityName,queryString)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK)
            loadLargeIconAndNotification(intent,code,title,message,image,isProfileIcon)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun prepareNotification(title: String,message: String,url: String,code: Int,image: String?,isProfileIcon: Boolean){
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            loadLargeIconAndNotification(browserIntent,code,title,message,image,isProfileIcon)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    private fun loadLargeIconAndNotification(intent:Intent,code:Int,title:String,message:String,url:String?,isProfileIcon: Boolean){
        val finish_target = object : CustomTarget<Bitmap>() {

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                makeNotification(intent,code,title,message,resource,isProfileIcon)
            }

            override fun onLoadStarted(placeholder: Drawable?) {
                super.onLoadStarted(placeholder)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                makeNotification(intent,code,title,message,null,isProfileIcon)

            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }
        }

        Glide.with(this)
            .asBitmap()
            .load(url).into(finish_target)
    }


    private fun makeNotification(intent:Intent,code:Int,title:String,message:String,icon:Bitmap?,isProfileIcon:Boolean){
        val pendingIntent:PendingIntent = PendingIntent.getActivity(this,code,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        mNotificationHelper = NotificationHelper(this)

        if(icon!=null){
            if(isProfileIcon){
                mNotificationHelper.notify(code,mNotificationHelper.getNotificationWithProfileIcon(title,message,pendingIntent,icon))
            }else{
                mNotificationHelper.notify(code,mNotificationHelper.getNotificationWithBannerIcon(title,message,pendingIntent,icon))
            }
        }else{
            mNotificationHelper.notify(code,mNotificationHelper.getNotificationWithText(title,message,pendingIntent))
        }
    }

}