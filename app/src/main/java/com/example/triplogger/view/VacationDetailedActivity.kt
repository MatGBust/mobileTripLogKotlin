package com.example.triplogger.view

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.triplogger.R
import com.example.triplogger.model.Vacation
import com.example.vacationlogger.viewModel.VacationViewModel

private const val TAG = "VacationDetailedActivity"

class VacationDetailedActivity : AppCompatActivity() {
    private val vacationViewModel: VacationViewModel by viewModels()
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var notesTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacation_detalied)

        titleTextView = findViewById<TextView>(R.id.titleTextView)
        descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)
        dateTextView = findViewById<TextView>(R.id.dateTextView)
        locationTextView = findViewById<TextView>(R.id.locationTextView)
        notesTextView = findViewById<TextView>(R.id.notesTextView)


        // Retrieve the vacation ID from the intent
        val vacationId = intent.getIntExtra("VACATION_ID", -1)

        // Check if the vacationId is valid
        if (vacationId != -1) {
            // Observe the vacation details
            vacationViewModel.getVacationById(vacationId).observe(this, Observer { vacation ->
                if (vacation != null) {
                    displayVacationDetails(vacation)
                } else {
                    Log.e(TAG, "Vacation not found with ID: $vacationId")
                    // Handle the case where vacation is not found
                }
            })
        } else {
            Log.e(TAG, "Invalid vacation ID")
            // Handle invalid ID case
        }
    }

    private fun displayVacationDetails(vacation: Vacation) {
        titleTextView.text = vacation.title
        descriptionTextView.text = vacation.description
        dateTextView.text = vacation.date
        locationTextView.text = vacation.location
        notesTextView.text = vacation.notes
    }
}
