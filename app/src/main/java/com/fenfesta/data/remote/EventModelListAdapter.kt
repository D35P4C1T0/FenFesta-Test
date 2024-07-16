package com.fenfesta.data.remote

import com.fenfesta.model.EventModel
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class EventModelListAdapter {
    @FromJson
    fun fromJson(jsonList: List<Map<String, Any?>>): List<EventModel> {
        return jsonList.mapNotNull { json ->
            try {
                EventModel(
                    capacity = (json["capacity"] as? Number)?.toInt() ?: 0,
                    capacity_left = (json["capacity_left"] as? Number)?.toInt() ?: 0,
                    created_at = json["created_at"] as? String ?: "",
                    creator = (json["creator"] as? Number)?.toInt() ?: 0,
                    date = parseDateTime(json["date"] as? String),
                    description = json["description"] as? String ?: "",
                    id = (json["id"] as? Number)?.toInt() ?: 0,
                    location = json["location"] as? String ?: "",
                    name = json["name"] as? String ?: "",
                    lat = json["lat"] as? String ?: "",
                    lon = json["lon"] as? String ?: "",
                    tags = json["tags"] as? String ?: "",
                )
            } catch (e: Exception) {
                //println("Error parsing event: $e")
                null
            }
        }
    }

    private fun parseDateTime(dateString: String?): LocalDateTime {
        return try {
            if (dateString.isNullOrEmpty()) {
                LocalDateTime.now()
            } else {
                ZonedDateTime.parse(dateString).toLocalDateTime()
            }
        } catch (e: Exception) {
            //println("Error parsing date: $dateString")
            LocalDateTime.now()
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
                "tags" to event.tags,
            )
        }
    }
}