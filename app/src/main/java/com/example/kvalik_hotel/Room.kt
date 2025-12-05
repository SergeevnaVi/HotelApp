package com.example.kvalik_hotel

data class Room(
    val id: Int,
    val roomNumber: String,
    val roomType: String,
    val capacity: Int,
    val pricePerNight: Double,
    val description: String,
    val imagePath: String,
    val hasWifi: Boolean = true,
    val hasTV: Boolean = true,
    val hasAirConditioning: Boolean = true,
    val hasMinibar: Boolean = false,
    val hasBalcony: Boolean = false
)
