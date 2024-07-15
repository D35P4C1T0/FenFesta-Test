package com.example.logintest.data.notifications
import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.logintest.MainActivity
import com.example.logintest.R

class NotificationReceiver : BroadcastReceiver() {
    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("event_title") ?: "Evento"
        var description = intent.getStringExtra("event_description") ?: "Descrizione evento"
        val notificationId = intent.getIntExtra("notification_id", 0)

        // Create the notification channel if needed (for Android O and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Event Notifications"
            val descriptionText = "Notifiche per eventi salvati"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("event_channel", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification intent
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create the notification
        val notification = NotificationCompat.Builder(context, "event_channel")
            .setSmallIcon(R.drawable.logo_fen_festa)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Check if the permission is granted before notifying
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        } else {
            // Notification permission is not granted, show a fallback notification
            val fallbackNotification = NotificationCompat.Builder(context, "event_channel")
                .setSmallIcon(R.drawable.ic_launcher_monochrome)
                .setContentTitle("Notifiche disabilitate")
                .setContentText("Attiva le notifiche per ricevere avvisi sugli eventi.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request permission to show notifications
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
                // It's important to return here, as we've requested permission asynchronously
                return
            }
            NotificationManagerCompat.from(context).notify(notificationId + 1, fallbackNotification)
        }
    }
}


