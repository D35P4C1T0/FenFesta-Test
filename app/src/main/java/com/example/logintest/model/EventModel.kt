package com.example.logintest.model

data class EventModel(
    val capacity: Int,
    val capacity_left: Int,
    val created_at: String,
    val creator: Int,
    val date: String,
    val description: String,
    val id: Int,
    val location: String,
    val name: String
) {
    override fun toString(): String {
        return "EventModel(capacity=$capacity, capacity_left=$capacity_left, created_at='$created_at', creator=$creator, date='$date', description='$description', id=$id, location='$location', name='$name')"
    }
}