package com.example.logintest.data.remote

import com.example.logintest.model.EventModel
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class EventModelListAdapter {
    @FromJson
    fun fromJson(jsonList: List<Map<String, Any?>>): List<EventModel> {
        return jsonList.map { json ->
            EventModel(
                capacity = (json["capacity"] as Double).toInt(),
                capacity_left = (json["capacity_left"] as Double).toInt(),
                created_at = json["created_at"] as String,
                creator = (json["creator"] as Double).toInt(),
                date = ZonedDateTime.parse(json["date"] as String).toLocalDateTime(),
                description = json["description"] as String,
                id = (json["id"] as Double).toInt(),
                location = json["location"] as String,
                name = json["name"] as String,
                lat = json["lat"] as String,
                lon = json["lon"] as String,
                color = json["color"] as String
            )
        }
    }

    @ToJson
    fun toJson(dataList: List<EventModel>): List<Map<String, Any?>> {
        return dataList.map { event ->
            mapOf(
                "capacity" to event.capacity,
                "capacity_left" to event.capacity_left,
                "created_at" to event.created_at,
                "creator" to event.creator,
                "date" to event.date.atZone(java.time.ZoneOffset.UTC)
                    .format(DateTimeFormatter.ISO_DATE_TIME),
                "description" to event.description,
                "id" to event.id,
                "location" to event.location,
                "name" to event.name,
                "lat" to event.lat,
                "lon" to event.lon,
                "color" to event.color
            )
        }
    }
}