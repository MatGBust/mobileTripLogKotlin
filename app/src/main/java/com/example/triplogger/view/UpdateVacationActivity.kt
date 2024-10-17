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

        val vacationTitle = intent.getStringExtra("VACATION_TITLE")
        val locationEditText = findViewById<EditText>(R.id.locationEditText)
        val dateEditText = findViewById<EditText>(R.id.dateEditText)
        val updateButton = findViewById<Button>(R.id.buttonUpdateVacation)

        // Load the current details and display them in EditTexts
        vacationViewModel.allVacations.observe(this, Observer { vacations ->
            val vacation = vacations.find { it.title == vacationTitle }
            vacation?.let {
                locationEditText.setText(it.location)
                dateEditText.setText(it.date)
            }
        })

        // Handle update button click
        updateButton.setOnClickListener {
            val updatedVacation = Vacation(
                title = vacationTitle ?: "",
                location = locationEditText.text.toString(),
                date = dateEditText.text.toString()
            )
            vacationViewModel.updateVacation(updatedVacation)
            finish() // Close the activity
        }
    }
}
