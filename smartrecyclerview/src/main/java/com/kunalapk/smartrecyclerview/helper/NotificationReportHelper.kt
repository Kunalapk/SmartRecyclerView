package com.kunalapk.smartrecyclerview.helper

import android.content.Intent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kunalapk.smartrecyclerview.service.MessagingService
import com.kunalapk.smartrecyclerview.utils.SmartRecyclerViewConstants
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object NotificationReportHelper {

    fun createNotificationReportToFirestore(uuid:String?,campaign_name:String?,version:String?){
        if(campaign_name!=null && uuid!=null){
            try{
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val currentDateandTime: String = sdf.format(Date())

                SmartLogger.debug("NotificationReportHelper - ", "Adding data to campaign")
                val database = Firebase.firestore
                val campaign= hashMapOf(
                    "has_tapped" to false,
                    "version_code" to version,
                    "created_on" to currentDateandTime
                )

                database.collection(campaign_name).document(uuid).set(campaign)
                    .addOnSuccessListener { documentReference ->
                        SmartLogger.debug("NotificationReportHelper - ", "DocumentSnapshot added ")
                    }
                    .addOnFailureListener { e ->
                        SmartLogger.debug("NotificationReportHelper - ", "Error adding document :"+ e.toString())
                    }

            }catch (e: Exception){

            }

        }else{
            SmartLogger.debug("NotificationReportHelper - ", "Null values found in campaign")
        }

    }

    fun updateNotificationReport(intent: Intent?){
        val campaign_name = intent?.extras?.getString(SmartRecyclerViewConstants.KEY_NOTIFICATION_REPORT_CAMPAIGN_NAME)
        val uuid = intent?.extras?.getString(SmartRecyclerViewConstants.KEY_NOTIFICATION_REPORT_UUID)

        if(campaign_name!=null && uuid!=null){
            updateNotificationReport(uuid,campaign_name)
        }
    }

    fun updateNotificationReport(uuid:String,campaign_name:String){
        try{
            val database = Firebase.firestore
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentDateandTime: String = sdf.format(Date())


            val campaign = mapOf(
                "has_tapped" to true,
                "tapped_on" to currentDateandTime
            )

            database.collection(campaign_name).document(uuid).update(campaign)
                .addOnSuccessListener { documentReference ->
                    SmartLogger.debug("NotificationReportHelper - ", "DocumentSnapshot updated ")
                }
                .addOnFailureListener { e ->
                    SmartLogger.debug("NotificationReportHelper - ", "Error updating document :"+ e.toString())
                }

        }catch (e:Exception){

        }
    }

}