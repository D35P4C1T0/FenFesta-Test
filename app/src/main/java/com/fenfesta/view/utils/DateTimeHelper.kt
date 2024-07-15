package com.fenfesta.view.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object DateTimeHelper {
    fun createLocalDateTime(dateString: String, timeString: String): LocalDateTime {
        // Define input formatters
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        // Parse the date and time
        val date = LocalDate.parse(dateString, dateFormatter)
        val time = LocalTime.parse(timeString, timeFormatter)

        // Combine date and time
        val combinedDateTime = LocalDateTime.of(date, time)

        println("Combined DateTime: $combinedDateTime")

        // Format to the desired output
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val formattedString = combinedDateTime.atOffset(ZoneOffset.UTC).format(outputFormatter)

        println("Formatted String: $formattedString")

        // Parse the formatted string back to LocalDateTime
        return LocalDateTime.parse(formattedString, DateTimeFormatter.ISO_DATE_TIME)
    }
}