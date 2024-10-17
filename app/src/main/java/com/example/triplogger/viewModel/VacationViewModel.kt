package com.example.vacationlogger.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.triplogger.model.VacationRepository
import com.example.triplogger.model.Vacation
import com.example.triplogger.model.VacationDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VacationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VacationRepository
    val allVacations: LiveData<List<Vacation>>

    init {
        // Initialize the database and repository
        val vacationDao = VacationDatabase.getDatabase(application).vacationDao()
        repository = VacationRepository(vacationDao)
        allVacations = repository.allVacations
    }

    // Insert a new vacation asynchronously
    fun addVacation(vacation: Vacation)  {
        viewModelScope.launch(Dispatchers.IO){
            repository.addVacation(vacation)
        }
    }

    fun deleteVacation(vacation: Vacation){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteVacation(vacation) // Call delete method in the repository
        }
    }

    fun updateVacation(vacation: Vacation) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateVacation(vacation)
        }
    }
}
