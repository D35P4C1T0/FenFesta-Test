package com.fenfesta.ui.theme.screens.calendar

// colors
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fenfesta.model.EventModel
import com.fenfesta.ui.theme.Selection
import com.fenfesta.view.components.EventList
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.YearMonth
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar(
    modifier: Modifier,
    eventsList: List<EventModel>,
    onEventClick: (EventModel) -> Unit,
    updateEvents: () -> Unit,
    updateCurrentMonth: (YearMonth) -> Unit,
) {

//    Log.d("Compose", "Cal is recomposing")
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    var selection by remember { mutableStateOf<CalendarDay?>(null) }
    val daysOfWeek = remember { daysOfWeek() }

    LaunchedEffect(42) {
        Log.d("EventView", "first calendar LaunchedEffect")
        updateEvents()
    }


    // Derive eventsByDate from allEvents
    val eventsByDate by remember(eventsList) {
        derivedStateOf {
            eventsList.groupBy { it.date.toLocalDate() }
        }
    }

    val eventsInSelectedDate by remember(selection, eventsByDate) {
        derivedStateOf {
            val date = selection?.date
            if (date == null) emptyList() else eventsByDate[date].orEmpty()
        }
    }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first(),
        outDateStyle = OutDateStyle.EndOfGrid,
    )
    val coroutineScope = rememberCoroutineScope()
    val visibleMonth = rememberFirstCompletelyVisibleMonth(state)

    LaunchedEffect(visibleMonth) {
        // Clear selection if we scroll to a new month.
        selection = null
        // Update the current month and fetch new events
//        viewModel.updateCurrentMonth(visibleMonth.yearMonth)
        updateCurrentMonth(visibleMonth.yearMonth)
    }

    PullToRefreshBox(
        modifier = modifier,
        state = refreshState,
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            coroutineScope.launch {
                delay(500)
                updateEvents()
                isRefreshing = false
            }
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.shapes.medium
                        ),
                    colors = CardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                ) {
                    SimpleCalendarTitle(
                        modifier = Modifier.padding(8.dp),
                        currentMonth = visibleMonth.yearMonth,
                        goToPrevious = {
                            coroutineScope.launch {
                                val newMonth = state.firstVisibleMonth.yearMonth.previousMonth
                                state.animateScrollToMonth(newMonth)
                                updateCurrentMonth(newMonth)
                            }
                        },
                        goToNext = {
                            coroutineScope.launch {
                                val newMonth = state.firstVisibleMonth.yearMonth.nextMonth
                                state.animateScrollToMonth(newMonth)
                                updateCurrentMonth(newMonth)
                            }
                        },
                    )
                    HorizontalCalendar(
                        modifier = Modifier
//                            .padding(top = 45.dp)
                            .wrapContentWidth(),
                        state = state,
                        dayContent = { day ->
                            val eventsNumber = if (day.position == DayPosition.MonthDate) {
                                eventsByDate[day.date]?.size ?: 0
                            } else {
                                0
                            }

                            Day(
                                day = day,
                                isSelected = selection == day,
                                eventsNumber = eventsNumber,
                            ) { clicked ->
                                selection = clicked
                            }
                        },
                        monthHeader = {
                            MonthHeader(
                                modifier = Modifier.padding(vertical = 8.dp),
                                daysOfWeek = daysOfWeek,
                            )
                        },
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                EventList(
                    modifier = Modifier.fillMaxWidth(),
                    events = eventsInSelectedDate,
                    onEventClick = onEventClick
                )
            }
        }
    }
}

@Composable
private fun Day(
    day: CalendarDay,
    isSelected: Boolean,
    eventsNumber: Int,
    onClick: (CalendarDay) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // This is important for square-sizing!
            .testTag("MonthDay")
            .padding(8.dp)
//            .clip(CircleShape)
            .background(color = if (isSelected) Selection else Color.Transparent)
            // Disable clicks on inDates/outDates
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) },
            ),
        contentAlignment = Alignment.Center,
    ) {
        val textColor = when (day.position) {
            // Color.Unspecified will use the default text color from the current theme
            DayPosition.MonthDate -> if (isSelected) Color.White else Color.Unspecified
            DayPosition.InDate, DayPosition.OutDate -> Color.Gray
        }
        NumberBadge(number = eventsNumber) {
            Text(
                modifier = Modifier,
                text = day.date.dayOfMonth.toString(),
                color = textColor,
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
private fun MonthHeader(
    modifier: Modifier = Modifier,
    daysOfWeek: List<DayOfWeek> = emptyList(),
) {
    Row(modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                text = dayOfWeek.getDisplayName(
                    java.time.format.TextStyle.SHORT,
                    Locale.ENGLISH
                ),
                fontWeight = FontWeight.Light,
            )
        }
    }
}

@Composable
fun rememberFirstCompletelyVisibleMonth(state: CalendarState): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    // Only take non-null values as null will be produced when the
    // list is mid-scroll as no index will be completely visible.
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.visibleMonthsInfo.firstOrNull() }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month.month }
    }
    return visibleMonth.value
}

//@Composable
//fun NumberDot(number: Int, modifier: Modifier = Modifier) {
//    Box(
//        modifier = modifier
//            .size(20.dp)
//            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
//            .padding
//            .aspectRatio(1f),
////        contentAlignment = Alignment.TopStart,  // Changed from TopCenter to Center
//    ) {
//        Text(
//            text = number.toString(),
//            color = Color.White,
//            fontSize = 12.sp,
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.align(Alignment.Center)
//        )
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberBadge(
    number: Int,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = Modifier.size(24.dp)) {
        BadgedBox(
            modifier = Modifier.align(Alignment.TopCenter),
            badge = {
                if (number > 0) {
                    Badge(
                        modifier = Modifier.offset(x = 6.5.dp, y = (-6.5).dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ) {
                        Text(
                            text = number.toString(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            },
            content = content,
        )
    }
}