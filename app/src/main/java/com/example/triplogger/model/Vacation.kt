package com.example.triplogger.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacation_table")
data class Vacation(
    @PrimaryKey val title: String,
    val location: String,
    val date: String
)


