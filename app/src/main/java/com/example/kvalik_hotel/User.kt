package com.example.kvalik_hotel

data class User(
    val id: Int,
    val login: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val middleName: String
) {
    fun getFullName(): String {
        return "$lastName $firstName $middleName"
    }
}
