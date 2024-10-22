package com.example.triplogger.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.triplogger.R
import com.example.triplogger.model.Vacation
import com.example.vacationlogger.viewModel.VacationViewModel
import androidx.lifecycle.Observer

class UpdateVacationActivity : AppCompatActivity() {

    private val vacationViewModel: VacationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_vacation)

        val vacationTripId = intent.getIntExtra("VACATION_TRIP_ID", -1)
        val vacationTitle = intent.getStringExtra("VACATION_TITLE")
        val vacationDescription = intent.getStringExtra("VACATION_DESCRIPTION")
        val vacationLocation = intent.getStringExtra("VACATION_LOCATION")
        val vacationDate = intent.getStringExtra("VACATION_DATE")
        val vacationNotes = intent.getStringExtra("VACATION_NOTES")

        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val locationEditText = findViewById<EditText>(R.id.locationEditText)
        val dateEditText = findViewById<EditText>(R.id.dateEditText)
        val notesEditText = findViewById<EditText>(R.id.notesEditText)
        val updateButton = findViewById<Button>(R.id.buttonUpdateVacation)

        // Set the current vacation details into the EditTexts
        titleEditText.setText(vacationTitle)
        descriptionEditText.setText(vacationDescription)
        locationEditText.setText(vacationLocation)
        dateEditText.setText(vacationDate)
        notesEditText.setText(vacationNotes)

        updateButton.setOnClickListener {
            val updatedVacation = Vacation(
                tripId = vacationTripId,
                title = titleEditText.text.toString(),
                description = descriptionEditText.text.toString(),
                location = locationEditText.text.toString(),
                date = dateEditText.text.toString(),
                notes = notesEditText.text.toString()
            )
            vacationViewModel.updateVacation(updatedVacation)
            finish() // Close the activity
        }
    }
}
