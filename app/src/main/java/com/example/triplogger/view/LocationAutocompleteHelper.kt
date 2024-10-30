package com.example.triplogger.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.Global.getString
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.example.triplogger.BuildConfig
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode


class LocationAutocompleteHelper(private val context: Context, private val autocompleteLauncher: ActivityResultLauncher<Intent>) {

    init {
        // Initialize the Places API
        if (!Places.isInitialized()) {
            Places.initialize(context.applicationContext, BuildConfig.MAP_API_KEY)
        }
    }

    // Launches the autocomplete intent
    fun launchAutocomplete() {
        val fields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS_COMPONENTS
        )

        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .setTypeFilter(TypeFilter.CITIES) // Set to only show cities
            .build(context as Activity)
        autocompleteLauncher.launch(intent)
    }

    // Parses the autocomplete result to retrieve city, state, country, latitude, and longitude
    fun handleAutocompleteResult(resultCode: Int, data: Intent?, locationEditText: EditText): LatLng? {
        return if (resultCode == Activity.RESULT_OK) {
            data?.let {
                val place = Autocomplete.getPlaceFromIntent(it)
                val addressComponents = place.addressComponents
                var city: String? = null
                var state: String? = null
                var country: String? = null

                addressComponents?.asList()?.forEach { component ->
                    when {
                        component.types.contains("locality") -> city = component.name
                        component.types.contains("administrative_area_level_1") -> state = component.name
                        component.types.contains("country") -> country = component.name
                    }
                }

                // Set the location name in the EditText
                val locationString: String = if(country != "United States") "$city, $country" else "$city, $state"
                locationEditText.setText(locationString)  // Display selected location name in EditText

                // Return LatLng for further usage
                place.latLng
            }
        } else {
            if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Toast.makeText(context, "Error in selecting location", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }
}
