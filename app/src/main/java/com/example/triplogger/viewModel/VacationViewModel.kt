package com.example.vacationlogger.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.triplogger.model.Vacation
import com.example.triplogger.model.VacationRepository
import com.example.triplogger.utilities.NoNetworkException
import com.google.firebase.database.FirebaseDatabase

class VacationViewModel(application: Application) : AndroidViewModel(application) {

    var repository: VacationRepository = VacationRepository(application)
    val allVacations: LiveData<List<Vacation>> = repository.allVacations

    // LiveData to expose network error messages
    private val _networkError = MutableLiveData<String>()
    val networkError: LiveData<String> get() = _networkError

    // Add a new vacation
    fun addVacation(vacation: Vacation) {
        try {
            repository.addVacation(vacation)
        } catch (e: NoNetworkException) {
            _networkError.value = e.message // Notify UI about network error
        }    }

    // Delete a vacation by ID
    fun deleteVacation(vacationId: String) {
        try {
            repository.deleteVacation(vacationId)
        } catch (e: NoNetworkException) {
            _networkError.value = e.message // Notify UI about network error
        }    }

    // Update an existing vacation
    fun updateVacation(vacationId: String, vacation: Vacation) {
        try {
            repository.updateVacation(vacationId, vacation)
        } catch (e: NoNetworkException) {
            _networkError.value = e.message // Notify UI about network error
        }    }

    // Retrieve a single vacation by ID
    fun getVacationById(vacationId: String): LiveData<Vacation?> {
        var vacation: LiveData<Vacation?> = MutableLiveData<Vacation?>()
        try {
            vacation = repository.getVacationById(vacationId)
        } catch (e: NoNetworkException) {
            _networkError.value = e.message // Notify UI about network error
        }
        return vacation
    }
}
