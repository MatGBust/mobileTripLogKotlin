package com.example.triplogger.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.triplogger.R
import com.example.triplogger.model.Vacation
import com.example.vacationlogger.viewModel.VacationViewModel
import androidx.lifecycle.Observer
import com.example.triplogger.utils.LocationAutocompleteHelper
import com.google.android.gms.maps.model.LatLng

class UpdateVacationActivity : AppCompatActivity() {

    private val vacationViewModel: VacationViewModel by viewModels()
    private lateinit var locationAutocompleteHelper: LocationAutocompleteHelper
    private var selectedLatLng: LatLng? = null // Variable to store latitude and longitude

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_vacation)

        val vacationTripId = intent.getIntExtra("VACATION_TRIP_ID", -1)
        val vacationTitle = intent.getStringExtra("VACATION_TITLE")
        val vacationDescription = intent.getStringExtra("VACATION_DESCRIPTION")
        val vacationLocation = intent.getStringExtra("VACATION_LOCATION")
        val vacationDate = intent.getStringExtra("VACATION_DATE")
        val vacationNotes = intent.getStringExtra("VACATION_NOTES")
        val vacationLatitude = intent.getDoubleExtra("VACATION_LATITUDE", 0.0) // Get existing latitude
        val vacationLongitude = intent.getDoubleExtra("VACATION_LONGITUDE", 0.0) // Get existing longitude


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

        // Initialize selectedLatLng to existing values
        selectedLatLng = LatLng(vacationLatitude, vacationLongitude)

        // Initialize AutocompleteHelper with a launcher for the autocomplete result
        val autocompleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // Handle the autocomplete result and get LatLng
            selectedLatLng = locationAutocompleteHelper.handleAutocompleteResult(result.resultCode, result.data, locationEditText)
        }
        locationAutocompleteHelper = LocationAutocompleteHelper(this, autocompleteLauncher)

        // Launch autocomplete when location EditText is clicked
        locationEditText.setOnClickListener {
            locationAutocompleteHelper.launchAutocomplete()
        }

        updateButton.setOnClickListener {
            val updatedVacation = Vacation(
                tripId = vacationTripId,
                title = titleEditText.text.toString(),
                description = descriptionEditText.text.toString(),
                location = locationEditText.text.toString(),
                date = dateEditText.text.toString(),
                latitude = selectedLatLng?.latitude,
                longitude = selectedLatLng?.longitude,
                notes = notesEditText.text.toString()
            )
            vacationViewModel.updateVacation(updatedVacation)
            finish() // Close the activity
        }
    }
}
