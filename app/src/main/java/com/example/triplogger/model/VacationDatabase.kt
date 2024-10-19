package com.example.triplogger.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Vacation::class], version = 2, exportSchema = false)
abstract class VacationDatabase : RoomDatabase() {

    abstract fun vacationDao(): VacationDao

    companion object {
        @Volatile
        private var INSTANCE: VacationDatabase? = null

        fun getDatabase(context: Context): VacationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VacationDatabase::class.java,
                    "vacation_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}