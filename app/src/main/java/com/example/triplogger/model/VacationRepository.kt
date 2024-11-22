package com.example.triplogger.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.triplogger.utilities.NetworkUtils
import com.example.triplogger.utilities.NoNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class VacationRepository(private val context: Context) {

    private val firebaseDb = FirebaseDatabase.getInstance()
    private val userVacationsRef = firebaseDb.reference.child("users")
        .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
        .child("vacations")

    // LiveData to observe vacation data from Firebase
    private val _allVacations = MutableLiveData<List<Vacation>>()
    val allVacations: LiveData<List<Vacation>> get() = _allVacations

    init {
        // Load data in real-time from Firebase
        userVacationsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val vacationList = mutableListOf<Vacation>()
                for (vacationSnapshot in snapshot.children) {
                    val vacation = vacationSnapshot.getValue(Vacation::class.java)
                    vacation?.let { vacationList.add(it) }
                }
                _allVacations.value = vacationList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }

    // Add a new vacation to Firebase
    fun addVacation(vacation: Vacation) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            throw NoNetworkException("No Internet connection available.")
        }
        val newVacationRef = userVacationsRef.push() // Create a new unique key for each vacation
        val vacationId = newVacationRef.key
        val vacationWithId = vacation.copy(id = vacationId)
        newVacationRef.setValue(vacationWithId)
    }

    // Delete a vacation from Firebase
    fun deleteVacation(vacationId: String) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            throw NoNetworkException("No Internet connection available.")
        }
        userVacationsRef.child(vacationId).removeValue()
    }

    // Update an existing vacation in Firebase
    fun updateVacation(vacationId: String, updatedVacation: Vacation) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            throw NoNetworkException("No Internet connection available.")
        }
        userVacationsRef.child(vacationId).setValue(updatedVacation)
    }

    // Fetch a single vacation by its ID
    fun getVacationById(vacationId: String): LiveData<Vacation?> {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            throw NoNetworkException("No Internet connection available.")
        }
        val vacationLiveData = MutableLiveData<Vacation?>()
        userVacationsRef.child(vacationId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val vacation = snapshot.getValue(Vacation::class.java)
                vacationLiveData.value = vacation
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("VacationRepository", "Failed to retrieve vacation with ID: $vacationId", error.toException())
                vacationLiveData.value = null
            }
        })
        return vacationLiveData
    }
}
