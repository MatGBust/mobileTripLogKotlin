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
        val editTextDescription = findViewById<EditText>(R.id.editTextDescription)
        val editTextDate = findViewById<EditText>(R.id.editTextDate)
        val editTextLocation = findViewById<EditText>(R.id.editTextLocation)
        val editTextNotes = findViewById<EditText>(R.id.editTextNotes)

        val buttonAddVacation = findViewById<Button>(R.id.buttonAddVacation)

        buttonAddVacation.setOnClickListener {
            val titleTxt = editTextTitle.text.toString()
            val descriptionTxt = editTextDescription.text.toString()
            val dateTxt = editTextDate.text.toString()
            val locationTxt = editTextLocation.text.toString()
            val notesTxt = editTextNotes.text.toString()


            if (titleTxt.isNotBlank() && locationTxt.isNotBlank() && dateTxt.isNotBlank()) {
                val newVacation = Vacation(title = titleTxt, description = descriptionTxt, date =  dateTxt, location =  locationTxt, notes = notesTxt )
                vacationViewModel.addVacation(newVacation)
                finish() // Close the activity after adding the vacation
            } else {
                // TODO: Handle error here
            }
        }
    }
}