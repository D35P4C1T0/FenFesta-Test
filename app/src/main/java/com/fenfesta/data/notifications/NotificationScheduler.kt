package com.fenfesta.data.notifications

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.time.LocalDateTime
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class NotificationScheduler(private val context: Context) {

    fun scheduleNotification(eventId: Int, eventName: String, eventTime: LocalDateTime) {
        val currentTime = LocalDateTime.now()
        val eventTimeMillis =
            eventTime.atZone(TimeZone.getDefault().toZoneId()).toInstant().toEpochMilli()
        // 1 hour before the event start
        val delay = eventTimeMillis - currentTime.atZone(TimeZone.getDefault().toZoneId())
            .toInstant().toEpochMilli() - TimeUnit.HOURS.toMillis(1)


        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    "EVENT_ID" to eventId,
                    "EVENT_NAME" to eventName
                )
            )
            .addTag("event_$eventId")
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "event_notification_$eventId",
            ExistingWorkPolicy.REPLACE,
            notificationWork
        )
    }

    fun cancelNotification(eventId: Int) {
        WorkManager.getInstance(context).cancelAllWorkByTag("event_$eventId")
    }
}