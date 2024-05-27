package com.example.logintest.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.YearMonth
import java.util.Locale


@Composable
fun Calendar() {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    Column(modifier = Modifier.padding(16.dp)) {
        MonthHeader(currentMonth, onPrevMonth = {
            currentMonth = currentMonth.minusMonths(1)
        }, onNextMonth = {
            currentMonth = currentMonth.plusMonths(1)
        })
        WeekDaysHeader()
        DaysGrid(currentMonth)
    }
}

@Composable
fun MonthHeader(yearMonth: YearMonth, onPrevMonth: () -> Unit, onNextMonth: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevMonth) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
        }
        Text(
            text = "${
                yearMonth.month.getDisplayName(
                    java.time.format.TextStyle.SHORT,
                    Locale.getDefault()
                )
            } ${yearMonth.year}",
            style = MaterialTheme.typography.titleLarge
        )
        IconButton(onClick = onNextMonth) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
        }
    }
}

@Composable
fun WeekDaysHeader() {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    Row(modifier = Modifier.fillMaxWidth()) {
        daysOfWeek.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun DaysGrid(yearMonth: YearMonth) {
    val firstOfMonth = yearMonth.atDay(1)
    val lastOfMonth = yearMonth.atEndOfMonth()
    val firstDayOfWeek =
        firstOfMonth.dayOfWeek.value % 7 // Monday is 1, need to adjust for our week starting with Monday
    val daysInMonth = (1..lastOfMonth.dayOfMonth).toList()
    val totalCells = firstDayOfWeek + daysInMonth.size
    val rows = totalCells / 7 + if (totalCells % 7 > 0) 1 else 0

    Column {
        for (row in 0 until rows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val index = row * 7 + col
                    if (index >= firstDayOfWeek && index < firstDayOfWeek + daysInMonth.size) {
                        Text(
                            text = daysInMonth[index - firstDayOfWeek].toString(),
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}