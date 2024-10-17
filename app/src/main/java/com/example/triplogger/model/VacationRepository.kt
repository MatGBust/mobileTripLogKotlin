package com.example.triplogger.model

import androidx.lifecycle.LiveData

class VacationRepository(private val vacationDao: VacationDao) {

    // Return all vacations as
    val allVacations: LiveData<List<Vacation>> = vacationDao.getAllVacations()

    // Insert a vacation asynchronously using Coroutines
    suspend fun addVacation(vacation: Vacation) {
        vacationDao.addVacation(vacation)
    }

    suspend fun deleteVacation(vacation: Vacation){
        vacationDao.deleteVacation(vacation)
    }

    // Add updateVacation function
    suspend fun updateVacation(vacation: Vacation) {
        vacationDao.updateVacation(vacation)
    }

}
