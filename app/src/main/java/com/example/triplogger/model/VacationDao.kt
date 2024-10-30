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

    @Delete
    suspend fun deleteVacation(vacation: Vacation)

    @Update
    suspend fun updateVacation(vacation: Vacation)

    @Query("SELECT * FROM vacation_table WHERE tripId = :vacationId")
    fun getVacationByVacationId(vacationId: Int): LiveData<Vacation>
}
