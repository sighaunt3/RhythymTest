package com.example.bluetoothtest

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BackgroundService: Service() {
    val apihandler = Handler()
    var count = 0
    var heart_rate = 0.0
    private val sharedview: SharedViewModel
        get() = (application as Helper).sharedViewModel
    private val broadcastRunnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            GlobalScope.launch {

                println("has run")
                var i:Double = 0.0
                println(sharedview.age_data.value)
                if(sharedview.heart_rate.value != ""){
                     i = 70.00
                }
                else{
                    println("HELLO")
                     i = sharedview.heart_rate.value.toString().toDouble()
                }

                println(i)
                var x = sharedview.hr.value?.plus(i)
                sharedview.hr.postValue(x)
                count += 1
                println(sharedview.hr.value)
                if(count == 60){
                    println(count)

                    count = 0
                    println(count)

                    if(sharedview.tmp2.value == true){
                        sharedview.tmp2.postValue(false)
                        sharedview.hr.postValue(heart_rate.toString().toDouble())


                    }
                    else{
                        sharedview.tmp2.postValue(true)
                        sharedview.hr.postValue(heart_rate.toString().toDouble())


                    }

                }
            }
            apihandler.postDelayed(this,1000)
        }

    }

    override fun onCreate() {
        super.onCreate()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "Foreground Service Channel"
            val descriptionText = "Foreground service channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("ForegroundServiceChannel", name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification for the foreground service
        val notification: Notification = createNotification()

        // Start the service as a foreground service with the notification
        startForeground(2, notification)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        apihandler.post(broadcastRunnable)
        return START_STICKY

    }

    private fun createNotification(): Notification {
        val notificationTitle = "Watch Service"
        val notificationText = "Service is running in the background"
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).apply {
                // Add the extra value for fragment identification
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            },
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, "ForegroundServiceChannel")
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        apihandler.removeCallbacks(broadcastRunnable)
    }
}