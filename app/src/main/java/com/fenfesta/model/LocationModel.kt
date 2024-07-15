package com.fenfesta.model

data class LocationModel(
    val lat: Double,
    val lon: Double,
    val address: String,
    val streetNumber: String,
    val city: String,
    ) {
    override fun toString(): String {
        return "$address, $streetNumber, $city"
    }
}