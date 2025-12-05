package com.example.kvalik_hotel

data class Booking(
    val id: Int,
    val roomId: Int,
    val checkInDate: String,
    val checkOutDate: String,
    val guestCount: Int
)
