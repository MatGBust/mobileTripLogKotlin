package com.example.triplogger.view

import android.os.Bundle
import android.util.Log
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
private const val TAG = "UpdateVacationActivity"

class UpdateVacationActivity : AppCompatActivity() {

    private val vacationViewModel: VacationViewModel by viewModels()
    private lateinit var locationAutocompleteHelper: LocationAutocompleteHelper
    private var selectedLatLng: LatLng? = null // Variable to store latitude and longitude
    private lateinit var titleEditText : EditText
    private lateinit var descriptionEditText : EditText
    private lateinit var locationEditText : EditText
    private lateinit var dateEditText : EditText
    private lateinit var notesEditText : EditText
    private lateinit var updateButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_vacation)

        titleEditText = findViewById<EditText>(R.id.titleEditText)
        descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        locationEditText = findViewById<EditText>(R.id.locationEditText)
        dateEditText = findViewById<EditText>(R.id.dateEditText)
        notesEditText = findViewById<EditText>(R.id.notesEditText)
        updateButton = findViewById<Button>(R.id.buttonUpdateVacation)

        // Retrieve the vacation ID from the intent
        val vacationId = intent.getIntExtra("VACATION_ID", -1)

        // Check if the vacationId is valid
        if (vacationId != -1) {
            // Observe the vacation details
            vacationViewModel.getVacationById(vacationId).observe(this, Observer { vacation ->
                if (vacation != null) {
                    // Set the current vacation details into the EditTexts
                    titleEditText.setText(vacation.title)
                    descriptionEditText.setText(vacation.description)
                    locationEditText.setText(vacation.location)
                    dateEditText.setText(vacation.date)
                    notesEditText.setText(vacation.notes)

                    // Initialize selectedLatLng to existing values
                    if (vacation.latitude != null && vacation.longitude != null) selectedLatLng = LatLng(vacation.latitude, vacation.longitude)

                } else {
                    Log.e(TAG, "Vacation not found with ID: $vacationId")
                    // Handle the case where vacation is not found
                }
            })
        } else {
            Log.e(TAG, "Invalid vacation ID")
            // Handle invalid ID case
        }

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
                tripId = vacationId,
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

    private fun setEditTexts(vacation: Vacation) {

    }
}
