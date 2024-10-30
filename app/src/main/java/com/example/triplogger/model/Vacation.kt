package com.example.triplogger.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.io.Console

@Entity(tableName = "vacation_table")
data class Vacation(
    @PrimaryKey(autoGenerate = true) val tripId: Int = 0,
    val title: String,
    val description: String = "",
    val date: String = "",
    val location: String = "",
    val latitude: Double?,
    val longitude: Double?,
    val notes: String = ""
)


