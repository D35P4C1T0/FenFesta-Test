package com.example.logintest.ui.utils

import com.example.logintest.model.EventModel
import java.time.LocalDateTime
import java.time.YearMonth
import kotlin.random.Random

object EventGenerator {
    fun generateEvents(): List<EventModel> = buildList {
        val currentMonth = YearMonth.now()
        var eventId = 1

        fun createEvent(date: LocalDateTime, name: String, location: String): EventModel {
            return EventModel(
                capacity = Random.nextInt(50, 200),
                capacity_left = Random.nextInt(0, 50),
                created_at = LocalDateTime.now().minusDays(Random.nextLong(1, 30)).toString(),
                creator = Random.nextInt(1, 1000),
                date = date,
                description = "Join us for $name in $location!",
                id = eventId++,
                location = location,
                name = name,
                lat = Random.nextDouble(4.0, 14.0),
                lon = Random.nextDouble(2.0, 15.0),
                color = randomHexColor(),
            )
        }

        val eventData = listOf(
            "Tech Conference" to "Lagos",
            "Music Festival" to "Enugu",
            "Food Fair" to "Ibadan",
            "Art Exhibition" to "Sokoto",
            "Movie Premiere" to "Makurdi",
            "Book Reading" to "Kaduna",
            "Marathon" to "Kano",
            "Science Fair" to "Minna",
            "Comedy Night" to "Asaba",
            "Cultural Festival" to "Port Harcourt"
        )

        // Generate 10 random days within the current month
        val eventDays = generateSequence { Random.nextInt(1, currentMonth.lengthOfMonth() + 1) }
            .distinct()
            .take(10)
            .sorted()
            .toList()

        eventDays.zip(eventData).forEach { (day, eventInfo) ->
            val eventDate = currentMonth.atDay(day).atTime(
                Random.nextInt(9, 20), // Event hour between 9 AM and 7 PM
                Random.nextInt(0, 60)  // Random minute
            )
            val (name, location) = eventInfo
            add(createEvent(eventDate, name, location))
        }
    }

    fun randomHexColor(): String {
        val red = Random.nextInt(256)
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)
        return "0xFF" + String.format("%02X%02X%02X", red, green, blue)
    }
}