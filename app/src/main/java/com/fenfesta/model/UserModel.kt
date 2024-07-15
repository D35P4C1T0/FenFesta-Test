package com.fenfesta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserModel(
    val id: Int,
//    val password: String,
    val username: String,
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String,
    val email: String,
    @Json(name = "is_staff")
    val isStaff: Boolean,
//    val eventsParticipated: Int,
    val profileImageUrl: String? = null,
    val isActive: Boolean = true,
    val dateJoined: String? = null
)