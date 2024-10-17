package com.example.triplogger.model
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "vacation_table")
data class Vacation(
    @PrimaryKey val title: String,
    val location: String,
    val date: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
    parcel.readString() ?: "",
    parcel.readString() ?: "",
    parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(location)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Vacation> {
        override fun createFromParcel(parcel: Parcel): Vacation {
            return Vacation(parcel)
        }

        override fun newArray(size: Int): Array<Vacation?> {
            return arrayOfNulls(size)
        }
    }
}


