package com.fenfesta.data.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fenfesta.R

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val eventId = inputData.getLong("EVENT_ID", -1)
        val eventName = inputData.getString("EVENT_NAME") ?: "Event"

        // Show notification
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            "event_reminders",
            "Event Reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, "event_reminders")
            .setSmallIcon(R.drawable.ic_launcher_monochrome)
            .setContentTitle("Preparati!")
            .setContentText("$eventName avrà inizio tra 1 ora!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(eventId.toInt(), notification)

        return Result.success()
    }
}