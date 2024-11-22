package com.example.triplogger.view


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.triplogger.R
import com.example.triplogger.model.Vacation
import com.example.triplogger.utils.LocationAutocompleteHelper
import com.example.vacationlogger.viewModel.VacationViewModel
import com.google.android.gms.maps.model.LatLng

class AddVacationActivity : BaseActivity() {

    private lateinit var locationAutocompleteHelper: LocationAutocompleteHelper
    private var selectedLatLng: LatLng? = null // Variable to store latitude and longitude


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vacation)

        val editTextTitle = findViewById<EditText>(R.id.editTextTitle)
        val editTextDescription = findViewById<EditText>(R.id.editTextDescription)
        val editTextDate = findViewById<EditText>(R.id.editTextDate)
        val editTextLocation = findViewById<EditText>(R.id.editTextLocation)
        val editTextNotes = findViewById<EditText>(R.id.editTextNotes)
        val buttonAddVacation = findViewById<Button>(R.id.buttonAddVacation)

        // Register for autocomplete result
        val autocompleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            selectedLatLng = locationAutocompleteHelper.handleAutocompleteResult(result.resultCode, result.data, editTextLocation)
        }
        // Initialize the AutocompleteHelper
        locationAutocompleteHelper = LocationAutocompleteHelper(this, autocompleteLauncher)

        // Launch autocomplete when location edit text is clicked
        editTextLocation.setOnClickListener {
            locationAutocompleteHelper.launchAutocomplete()
        }


        buttonAddVacation.setOnClickListener {
            val titleTxt = editTextTitle.text.toString()
            val descriptionTxt = editTextDescription.text.toString()
            val dateTxt = editTextDate.text.toString()
            val locationTxt = editTextLocation.text.toString()
            val notesTxt = editTextNotes.text.toString()


            if (titleTxt.isNotBlank() && locationTxt.isNotBlank() && dateTxt.isNotBlank()) {
                val newVacation = Vacation(title = titleTxt, description = descriptionTxt, date =  dateTxt, location =  locationTxt, latitude = selectedLatLng?.latitude, longitude = selectedLatLng?.longitude, notes = notesTxt )
                vacationViewModel.addVacation(newVacation)
                finish() // Close the activity after adding the vacation
            } else {
                // TODO: Handle error here
            }
        }
    }
}