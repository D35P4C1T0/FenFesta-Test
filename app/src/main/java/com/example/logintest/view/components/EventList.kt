package com.example.logintest.view.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.logintest.data.notifications.NotificationReceiver
import com.example.logintest.model.EventModel
import java.time.LocalDateTime
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventList(
    modifier: Modifier = Modifier.fillMaxHeight(),
    events: List<EventModel>,
    onEventClick: (EventModel) -> Unit,
) {
//    val events = EventGenerator.generateEvents() // use dummy data
    Column(
        modifier = modifier.padding(top = 6.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        events.forEach { event ->
            EventCard(
                event = event,
                onEventClick = onEventClick,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCard(
    event: EventModel,
    titleFontSize: TextUnit = MaterialTheme.typography.headlineSmall.fontSize.times(0.70f),
    titleFontWeight: FontWeight = FontWeight.Bold,
    shape: Shape = MaterialTheme.shapes.medium,
    padding: Dp = 16.dp,
    onEventClick: (EventModel) -> Unit,
) {
    val context = LocalContext.current
    val title = event.name
    val description = event.description
    val date = event.date
    val location = event.location
    val startTime = event.date.toLocalTime()
        .format(DateTimeFormatter.ofPattern("HH:mm"))
        .toString()
    val endTime = event.date.toLocalTime()
        .format(DateTimeFormatter.ofPattern("HH:mm"))
        .toString()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300, easing = LinearOutSlowInEasing
                )
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
            onEventClick(event)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = abbreviatedDate(date),
                    fontSize = titleFontSize,
                    fontWeight = titleFontWeight,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(0.55f))

                // add to favorites button, allign far right
                IconButton(modifier = Modifier.weight(1f), onClick = {
                    scheduleNotification(context, event)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Grade,
                        contentDescription = "Add to Favourites",
                    )
                }

                Text(
                    modifier = Modifier.weight(6f),
                    textAlign = TextAlign.End,
                    text = "$title - $location\n$startTime-$endTime",
                    fontSize = titleFontSize,
                    fontWeight = titleFontWeight,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun scheduleNotification(context: Context, event: EventModel) {
    val notificationTime = event.date.minusHours(1)
    val currentTime = LocalDateTime.now()

    val delay = notificationTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000 - currentTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000

    if (delay > 0) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("event_title", event.name)
            putExtra("event_content", "Your event at ${event.location} starts in an hour!")
            putExtra("notification_id", event.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            event.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent)
    }
}
fun abbreviatedDate(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("d\nMMM")
    return dateTime.format(formatter).uppercase()
}