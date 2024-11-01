package com.example.vacationlogger.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.triplogger.model.Vacation
import com.example.triplogger.model.VacationRepository

class VacationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VacationRepository = VacationRepository()
    val allVacations: LiveData<List<Vacation>> = repository.allVacations

    // Add a new vacation
    fun addVacation(vacation: Vacation) {
        repository.addVacation(vacation)
    }

    // Delete a vacation by ID
    fun deleteVacation(vacationId: String) {
        repository.deleteVacation(vacationId)
    }

    // Update an existing vacation
    fun updateVacation(vacationId: String, vacation: Vacation) {
        repository.updateVacation(vacationId, vacation)
    }

    // Retrieve a single vacation by ID
    fun getVacationById(vacationId: String): LiveData<Vacation?> {
        return repository.getVacationById(vacationId)
    }
}
