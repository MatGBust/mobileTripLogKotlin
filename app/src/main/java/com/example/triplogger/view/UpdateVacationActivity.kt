package com.example.triplogger.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.triplogger.R
import com.example.vacationlogger.viewModel.VacationViewModel
import androidx.activity.viewModels
import com.example.triplogger.model.Vacation

class UpdateVacationActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var dateEditText: EditText
    private val vacationViewModel: VacationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_vacation)

        titleEditText = findViewById(R.id.editTextTitle)
        locationEditText = findViewById(R.id.editTextLocation)
        dateEditText = findViewById(R.id.editTextDate)

        val vacation = intent.getParcelableExtra<Vacation>("VACATION")

        vacation?.let {
            titleEditText.setText(it.title)
            locationEditText.setText(it.location)
            dateEditText.setText(it.date)
        }

        val buttonUpdate = findViewById<Button>(R.id.buttonUpdateVacation)
        buttonUpdate.setOnClickListener {
            val updatedVacation = Vacation(
                title = titleEditText.text.toString(),
                location = locationEditText.text.toString(),
                date = dateEditText.text.toString()
            )
            vacationViewModel.updateVacation(updatedVacation)
            finish()
        }
    }
}
