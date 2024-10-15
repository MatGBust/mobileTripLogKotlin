package com.example.triplogger.view


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.triplogger.R
import com.example.triplogger.model.Vacation
import com.example.vacationlogger.viewModel.VacationViewModel

class AddVacationActivity : AppCompatActivity() {

    private val vacationViewModel: VacationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vacation)

        val editTextTitle = findViewById<EditText>(R.id.editTextTitle)
        val editTextLocation = findViewById<EditText>(R.id.editTextLocation)
        val editTextDate = findViewById<EditText>(R.id.editTextDate)
        val buttonAddVacation = findViewById<Button>(R.id.buttonAddVacation)

        buttonAddVacation.setOnClickListener {
            val title = editTextTitle.text.toString()
            val location = editTextLocation.text.toString()
            val date = editTextDate.text.toString()

            if (title.isNotBlank() && location.isNotBlank() && date.isNotBlank()) {
                val newVacation = Vacation(title, location, date)
                vacationViewModel.addVacation(newVacation)
                finish() // Close the activity after adding the vacation
            } else {
                // TODO: Handle error here
            }
        }
    }
}