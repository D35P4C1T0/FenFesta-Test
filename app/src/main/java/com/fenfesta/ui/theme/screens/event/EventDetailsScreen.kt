package com.fenfesta.ui.theme.screens.event

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.fenfesta.data.notifications.scheduleNotification
import com.fenfesta.data.viewmodel.EventViewModel
import com.fenfesta.data.viewmodel.ReservationDeletionState
import com.fenfesta.data.viewmodel.ReservationState
import com.fenfesta.data.viewmodel.UserViewModel
import com.fenfesta.model.EventModel
import java.time.format.DateTimeFormatter

@Composable
fun EventDetailsScreen(
    modifier: Modifier,
    event: EventModel,
    eventViewModel: EventViewModel,
    userViewModel: UserViewModel,
    hideJoinButton: Boolean,
    onBackPress: () -> Unit,
    onReserveClick: () -> Unit,
) {
    userViewModel.clearReservationDeleteState()
    userViewModel.clearReservationState()
    val context = LocalContext.current

    val creatorName by eventViewModel.eventCreator.collectAsState()
    event.id?.let { eventViewModel.fetchEventCreatorInfo(it) }

    println("creator $creatorName")

    AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background color based on event color
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(28.dp)
            ) {
                // Event description
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(text = event.description)

                Spacer(modifier = Modifier.height(24.dp))

                // Event details
                EventDetailItem(
                    icon = Icons.Default.DateRange,
                    label = "Data",
                    value = event.date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a"))
                )
                EventDetailItem(
                    icon = Icons.Default.LocationOn,
                    label = "Location",
                    value = event.location
                )
                EventDetailItem(
                    icon = Icons.Default.Groups,
                    label = "Capacità",
                    value = "${event.capacity_left}/${event.capacity} posti disponibili"
                )

                EventDetailItem(
                    icon = Icons.Default.Person,
                    label = "Creatore",
                    value = creatorName,
                )

                TagsList(
                    icon = Icons.Default.Tag, label = "Tags", value = event.tags
                )

                IconButton(onClick = { scheduleNotification(context, event) }) {
                    Icon(imageVector = Icons.Default.Grade, contentDescription = "Notifica")
                }

                Spacer(modifier = Modifier.height(32.dp))

                when (hideJoinButton) {
                    true -> {
                        event.id?.let {
                            ReservationDeletion(
                                viewModel = userViewModel,
                                eventId = it
                            )
                        }
                    } // cancel reservation
                    false -> {
                        event.id?.let {
                            MakeReservation(
                                viewModel = userViewModel,
                                eventId = it,
                            )
                        }
                    } // make reservation
                }
            }
        }

    }
}


@Composable
fun EventDetailItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary
        )
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(text = label, style = MaterialTheme.typography.titleSmall)
            Text(text = value, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsList(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val tagsArray = value.split(",")
        Icon(
            imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary
        )
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(text = label, style = MaterialTheme.typography.titleSmall)
//            Text(text = value, style = MaterialTheme.typography.bodySmall)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
            ) {
                tagsArray.forEach {
                    if (it.isNotBlank()) {
                        Tag(text = it)
                    }
                }
            }
        }
    }
}

@Composable
fun Tag(text: String) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun MakeReservation(viewModel: UserViewModel, eventId: Int) {
    val reservationCreationState by viewModel.reservationState.collectAsState()

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = { viewModel.addReservation(eventId.toString()) },
            enabled = reservationCreationState !is ReservationState.Loading,

            ) {
            Text("Prenota")
        }

        when (val state = reservationCreationState) {
            is ReservationState.Loading -> CircularProgressIndicator()
            is ReservationState.Success -> Text(
                state.message,
                color = MaterialTheme.colorScheme.primary
            )

            is ReservationState.Error -> Text(
                state.message,
                color = MaterialTheme.colorScheme.error
            )

            else -> {} // Initial state, do nothing
        }
    }
}

@Composable
fun ReservationDeletion(viewModel: UserViewModel, eventId: Int) {
    val reservationDeletionState by viewModel.reservationDeletionState.collectAsState()

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally)
    {
        Button(
            onClick = { viewModel.deleteReservation(eventId) },
            enabled = reservationDeletionState !is ReservationDeletionState.Loading
        ) {
            Text("Annulla Prenotazione")
        }

        when (val state = reservationDeletionState) {
            is ReservationDeletionState.Loading -> CircularProgressIndicator()
            is ReservationDeletionState.Success -> Text(
                state.message,
                color = MaterialTheme.colorScheme.primary
            )

            is ReservationDeletionState.Error -> Text(
                state.message,
                color = MaterialTheme.colorScheme.error
            )

            else -> {} // Initial state, do nothing
        }
    }
}