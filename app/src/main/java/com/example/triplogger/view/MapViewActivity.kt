package com.example.triplogger.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.triplogger.R
import com.example.vacationlogger.viewModel.VacationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.lifecycle.Observer

private const val TAG = "MapViewActivity"


class MapViewActivity : AppCompatActivity(), OnMapReadyCallback  {
    private val vacationViewModel: VacationViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate entered")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_view)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.i(TAG, "Map initialized")
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = false

        // Set the map starting coordinates to osu
        val osu = LatLng(40.001, -83.017)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(osu))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(1f))

        // Set the map type to Hybrid.
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)

        // Observe the LiveData from the ViewModel
        vacationViewModel.allVacations.observe(this, Observer { vacations ->
            // Update the RecyclerView with the new list of vacations
            vacations.forEach{
                vacation ->
                if(vacation.latitude != null && vacation.longitude != null) {
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(vacation.latitude, vacation.longitude))
                            .title(vacation.title)
                    )
                }
            }
        })


        googleMap.setOnMarkerClickListener {
            val intent = Intent(this, ListViewActivity::class.java)
            startActivity(intent)
            true
        }


    }
}