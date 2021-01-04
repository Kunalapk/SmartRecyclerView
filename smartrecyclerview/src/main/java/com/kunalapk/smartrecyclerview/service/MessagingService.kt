package com.kunalapk.smartrecyclerview.service

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.BuildConfig
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kunalapk.smartrecyclerview.BuildConfig.VERSION_CODE
import com.kunalapk.smartrecyclerview.R
import com.kunalapk.smartrecyclerview.helper.*
import com.kunalapk.smartrecyclerview.helper.NotificationHelper
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

open class MessagingService : FirebaseMessagingService() {

    private lateinit var mNotificationHelper: NotificationHelper
    private var version: String? = null
    private var campaign_name: String? = null
    private var uuid:String? = null


    companion object {
        private val TAG = MessagingService::class.java.simpleName
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        onMessageReceived(remoteMessage,null)
    }

    fun onMessageReceived(remoteMessage: RemoteMessage,version:String?){
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
            this.version = version

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

            if(dataObject.has("campaign_name")){
                campaign_name = dataObject.getString("campaign_name")
                uuid = UUID.randomUUID().toString()
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
            val intent = IntentHelper.getIntent(this,activity,queryString,uuid,campaign_name)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK)
            loadLargeIconAndNotification(intent,code,title,message,image,isProfileIcon)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    fun prepareNotification(title: String,message: String,activityName:String,queryString:String?,code: Int,image:String?,isProfileIcon: Boolean){

        try {
            val intent = IntentHelper.getIntent(this,activityName,queryString,uuid,campaign_name)
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

    fun prepareNotificationWithCustomView(contentView:RemoteViews,bigContentView:RemoteViews,url: String,code: Int,image: String?){
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            loadLargeIconAndNotificationWithContentView(browserIntent,code,contentView,bigContentView,image)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun loadLargeIconAndNotificationWithContentView(intent:Intent,code:Int,contentView:RemoteViews,bigContentView:RemoteViews,url:String?){
        loadLargeIconAndNotification(intent = intent,code = code,title = "",message = "",imageUrl = url,isProfileIcon = false,contentView = contentView,bigContentView = bigContentView)
    }

    private fun loadLargeIconAndNotification(intent:Intent,code:Int,title:String,message:String,url:String?,isProfileIcon: Boolean){
        loadLargeIconAndNotification(intent = intent,code = code,title = title,message = message,imageUrl = url,isProfileIcon = isProfileIcon,contentView = null,bigContentView = null)
    }


    private fun loadLargeIconAndNotification(intent:Intent,code:Int,title:String,message:String,imageUrl:String?,isProfileIcon: Boolean,contentView:RemoteViews?,bigContentView:RemoteViews?){
        val finish_target = object : CustomTarget<Bitmap>() {

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                makeNotification(intent,code,title,message,resource,isProfileIcon,contentView,bigContentView)
            }

            override fun onLoadStarted(placeholder: Drawable?) {
                super.onLoadStarted(placeholder)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                makeNotification(intent,code,title,message,null,isProfileIcon,contentView,bigContentView)

            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }
        }

        Glide.with(this)
            .asBitmap()
            .load(imageUrl).into(finish_target)
    }


    private fun makeNotification(intent:Intent,code:Int,title:String,message:String,icon:Bitmap?,isProfileIcon:Boolean,contentView:RemoteViews?,bigContentView:RemoteViews?){
        if(contentView!=null){
            makeNotification(intent,code,title,message,null,isProfileIcon,R.drawable.ic_notification,contentView,bigContentView)
        }else{
            makeNotification(intent,code,title,message,null,isProfileIcon)
        }
    }

    private fun makeNotification(intent:Intent,code:Int,title:String,message:String,icon:Bitmap?,isProfileIcon:Boolean){
        makeNotification(intent,code,title,message,icon,isProfileIcon, R.drawable.ic_notification,null,null)
        if(campaign_name!=null && uuid!=null){
            NotificationReportHelper.createNotificationReportToFirestore(uuid,campaign_name,version)
        }
    }


    private fun makeNotification(intent:Intent,code:Int,title:String,message:String,icon:Bitmap?,isProfileIcon:Boolean,appNotificationIcon:Int,contentView:RemoteViews?,bigContentView:RemoteViews?){
        val pendingIntent:PendingIntent = PendingIntent.getActivity(this,code,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        mNotificationHelper = NotificationHelper(this)

        if(icon!=null){
            if(isProfileIcon){
                mNotificationHelper.notify(code,mNotificationHelper.getNotificationWithProfileIcon(title,message,pendingIntent,icon,appNotificationIcon))
            }else{
                mNotificationHelper.notify(code,mNotificationHelper.getNotificationWithBannerIcon(title,message,pendingIntent,icon,appNotificationIcon))
            }
        }else if(contentView!=null){
            mNotificationHelper.notify(code,mNotificationHelper.getNotificationWithCustomView(contentView,bigContentView,pendingIntent))
        }else{
            mNotificationHelper.notify(code,mNotificationHelper.getNotificationWithText(title,message,pendingIntent,appNotificationIcon))
        }
    }

}