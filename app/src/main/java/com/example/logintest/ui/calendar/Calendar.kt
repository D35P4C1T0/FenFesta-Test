package com.example.logintest.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logintest.model.EventModel
import com.example.logintest.ui.theme.toComposeColor
import com.example.logintest.ui.utils.EventGenerator
import com.example.logintest.view.components.EventList
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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.YearMonth
import java.util.Locale
// colors
import com.example.logintest.ui.theme.*

private val events = EventGenerator.generateEvents().groupBy { it.date.toLocalDate() }

//private val pageBackgroundColor: Color @Composable get() = colorResource(R.color.)
//private val itemBackgroundColor: Color @Composable get() = colorResource(R.color.example_5_item_view_bg_color)
//private val toolbarColor: Color @Composable get() = colorResource(R.color.example_5_toolbar_color)
//private val selectedItemColor: Color @Composable get() = colorResource(R.color.example_5_text_grey)
private val inActiveTextColor = Color(0xFFD3D3D3)


@Composable
fun Calendar(modifier: Modifier) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    var selection by remember { mutableStateOf<CalendarDay?>(null) }
    val daysOfWeek = remember { daysOfWeek() }
    val eventsInSelectedDate = remember {
        derivedStateOf {
            val date = selection?.date
            if (date == null) emptyList() else events[date].orEmpty()
        }
    }
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
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
        }

        SimpleCalendarTitle(
            modifier = Modifier,
            currentMonth = visibleMonth.yearMonth,
            goToPrevious = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                }
            },
            goToNext = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                }
            },
        )
        HorizontalCalendar(
            modifier = Modifier.wrapContentWidth(),
            state = state,
            dayContent = { day ->
                val eventsNumber = if (day.position == DayPosition.MonthDate) {
                    events[day.date]?.size ?: 0
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
//        LazyColumn(modifier = Modifier.fillMaxWidth()) {
//            items(items = eventsInSelectedDate.value) { flight ->
//                EventInformation(flight)
//            }
//        }
        Spacer(modifier = Modifier.height(16.dp))
        EventList(modifier = Modifier.fillMaxWidth(), eventsData = eventsInSelectedDate.value)
    }
}

/*
@Composable
private fun Day(
    day: CalendarDay,
    isSelected: Boolean = false,
    colors: List<Color> = emptyList(),
    onClick: (CalendarDay) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // This is important for square-sizing!
            .padding(6.dp)
//            .background(color = itemBackgroundColor)
            // Disable clicks on inDates/outDates
            .clip(CircleShape)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) },
            ),
        contentAlignment = Alignment.Center,
    ) {
        val textColor = when (day.position) {
            DayPosition.MonthDate -> Color.Unspecified
            DayPosition.InDate, DayPosition.OutDate -> inActiveTextColor
        }
        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 3.dp, end = 4.dp),
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 12.sp,
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            for (color in colors) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .background(color),
                )
            }
        }
    }
}
*/

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
        Text(
            modifier = Modifier,
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 14.sp,
        )
        if (eventsNumber > 0) {
            NumberDot(
                number = eventsNumber,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 8.dp, y = (-8).dp)
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
private fun LazyItemScope.EventInformation(event: EventModel) {
    Row(
        modifier = Modifier
            .fillParentMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Box(
            modifier = Modifier
                .background(color = event.color.toComposeColor())
                .fillParentMaxWidth(1 / 7f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = event.name.uppercase(Locale.getDefault()),
                textAlign = TextAlign.Center,
                lineHeight = 17.sp,
                fontSize = 12.sp,
            )
        }
        Box(
            modifier = Modifier
//                .background(color = itemBackgroundColor)
                .weight(1f)
                .fillMaxHeight(),
        ) {
//            EventInformation(event.departure, isDeparture = true)
        }
        Box(
            modifier = Modifier
//                .background(color = itemBackgroundColor)
                .weight(1f)
                .fillMaxHeight(),
        ) {
//            EventInformation(event.destination, isDeparture = false)
        }
    }
//    Divider(color = pageBackgroundColor, thickness = 2.dp)
    Divider(thickness = 2.dp)
}

@Composable
private fun EventInformation(event: EventModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Column(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = event.capacity.toString(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = event.location,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
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

@Composable
fun NumberDot(number: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(24.dp)
            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}