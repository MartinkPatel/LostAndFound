package com.martin_kirtan.lostandfound.firestore

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.martin_kirtan.lostandfound.R
import java.text.SimpleDateFormat
import java.util.*

class MyFireBaseMessagingService: FirebaseMessagingService() {
    private val CHANNEL_ID = "MESSAGE"
    private val CHANNEL_NAME = "MESSAGE"
    private lateinit var notificationManagerCompat: NotificationManagerCompat
    private lateinit var title: String
    private lateinit var message: String
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        notificationManagerCompat = NotificationManagerCompat.from(applicationContext)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManagerCompat.createNotificationChannel(channel)
        }

        title = remoteMessage.getData().get("Title").toString()
        message = remoteMessage.getData().get("Message").toString()

        val notification: Notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)


            .setSmallIcon(R.drawable.lost_and_found_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManagerCompat.notify(getRandomNumber(), notification)

    }

    private fun getRandomNumber(): Int {
        val dd = Date()
        val ft = SimpleDateFormat("mmssSS")
        val s: String = ft.format(dd)
        return s.toInt()
    }

    private fun onNewToken(){
        val refreshToken:String= FirebaseMessaging.getInstance().token.toString()
        val token=Token(refreshToken)
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(token)
    }
}