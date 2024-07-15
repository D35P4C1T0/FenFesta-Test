package com.fenfesta.data.remote

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class LocalTimeAdapter {
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    @FromJson
    fun fromJson(value: String): LocalDateTime {
        return ZonedDateTime.parse(value, formatter).toLocalDateTime()
    }

    @ToJson
    fun toJson(value: LocalDateTime): String {
        return value.atZone(java.time.ZoneOffset.UTC).format(formatter)
    }
}