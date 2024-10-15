package com.example.triplogger.model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.triplogger.model.Vacation

@Dao
interface VacationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVacation(vacation: Vacation)

    @Query("SELECT * FROM vacation_table ORDER BY title ASC")
    fun getAllVacations(): LiveData<List<Vacation>>
}
