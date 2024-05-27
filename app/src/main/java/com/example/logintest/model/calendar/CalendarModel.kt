package com.example.logintest.model.calendar

import com.example.logintest.model.EventModel
import java.time.LocalDate

// model for a calendar
data class CalendarModel(
    private val date: LocalDate,
    val eventsList: List<EventModel>
)