package com.example.triplogger.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
    private lateinit var picturesButton: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacation_detalied)

        titleTextView = findViewById(R.id.titleTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        dateTextView = findViewById(R.id.dateTextView)
        locationTextView = findViewById(R.id.locationTextView)
        notesTextView = findViewById(R.id.notesTextView)
        picturesButton = findViewById(R.id.picturesButton)

        // Retrieve the Firebase vacation ID
        val vacationId = intent.getStringExtra("VACATION_ID")

        // Observe vacation data if the ID is valid
        if (vacationId != null) {
            vacationViewModel.getVacationById(vacationId).observe(this, Observer { vacation ->
                if (vacation != null) {
                    displayVacationDetails(vacation)
                } else {
                    Log.e(TAG, "Vacation not found with ID: $vacationId")
                }
            })
        } else {
            Log.e(TAG, "Invalid or null vacation ID received.")
        }
        picturesButton.setOnClickListener {
            val intent = Intent(this, PhotosActivity::class.java).apply {
                putExtra("VACATION_ID", vacationId)
            }
            startActivity(intent)
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