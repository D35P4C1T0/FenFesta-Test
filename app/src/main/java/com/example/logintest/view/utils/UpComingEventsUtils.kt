package com.example.logintest.view.utils

import com.example.logintest.model.EventModel
import java.time.LocalDate

class UpComingEventsUtils {
    // da usare per segnare i giorni con eventi nel calendario
    fun isDateInEventsList(list: List<EventModel>, date: LocalDate): Boolean {
        // parse the date from the event list
        for (event in list) {
            val eventDate = LocalDate.parse(event.date) // parse the date from the event list
            // TODO: Ma sta roba va?? ^^^
            if (eventDate == date) {
                return true
            }
        }
        return false
    }
}