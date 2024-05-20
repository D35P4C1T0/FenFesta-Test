package com.example.logintest.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@JsonClass(generateAdapter = true)
data class UserModel(
    val id: Int,
    val password: String,
    val username: String,
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String,
    val email: String,
    @Json(name = "is_staff")
    val isStaff: Boolean,
)

fun jsonToUser(json: String): UserModel {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter = moshi.adapter(UserModel::class.java)
    return jsonAdapter.fromJson(json)!!
}