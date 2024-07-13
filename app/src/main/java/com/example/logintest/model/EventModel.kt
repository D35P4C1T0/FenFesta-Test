package com.example.logintest.model

import java.time.LocalDateTime

data class EventModel(
    val capacity: Int,
    val capacity_left: Int,
    val created_at: String,
    val creator: Int,
    val date: LocalDateTime,
    val description: String,
    val id: Int? = null,
    val location: String,
    val name: String,
    val lat: String,
    val lon: String,
    val tags: String,
) {
    override fun toString(): String {
        return "EventModel(capacity=$capacity, capacity_left=$capacity_left, created_at='$created_at', creator=$creator, date='$date.to', description='$description', id=$id, location='$location', name='$name')"
    }
}