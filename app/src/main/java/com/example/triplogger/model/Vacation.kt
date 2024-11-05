package com.example.triplogger.model

data class Vacation(
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val location: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val notes: String = "",
    val photos: MutableList<String> = mutableListOf()
)
