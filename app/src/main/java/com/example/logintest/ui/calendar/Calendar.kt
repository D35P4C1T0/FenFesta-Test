package com.example.logintest.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logintest.model.EventModel
import com.example.logintest.ui.theme.toComposeColor
import com.example.logintest.ui.utils.EventGenerator
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
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale

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
    val flightsInSelectedDate = remember {
        derivedStateOf {
            val date = selection?.date
            if (date == null) emptyList() else events[date].orEmpty()
        }
    }
    Column(
        modifier = modifier
//            .background(pageBackgroundColor),
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
                val colors = if (day.position == DayPosition.MonthDate) {
                    events[day.date].orEmpty().map { it.color.toComposeColor() }
                } else {
                    emptyList()
                }
                Day(
                    day = day,
                    isSelected = selection == day,
                    colors = colors,
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
//        Divider(color = pageBackgroundColor)
        Divider()
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(items = flightsInSelectedDate.value) { flight ->
                EventInformation(flight)
            }
        }

    }
}

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
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = Color.White,
//                color = if (isSelected) selectedItemColor else Color.Transparent,
            )
            .padding(1.dp)
//            .background(color = itemBackgroundColor)
            // Disable clicks on inDates/outDates
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) },
            ),
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
                color = Color.White,
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