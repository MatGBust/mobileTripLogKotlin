package com.example.triplogger.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.triplogger.R
import com.example.vacationlogger.viewModel.VacationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.BitmapDescriptorFactory



private const val TAG = "MapViewActivity"
private const val REQUEST_LOCATION_PERMISSION = 2


class MapViewActivity : BaseActivity(), OnMapReadyCallback  {
    private val vacationViewModel: VacationViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userLocationMarker: Marker? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate entered")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_view)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.i(TAG, "Map initialized")
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = false

        // Set the map type to Hybrid.
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)



        // Default starting coordinates (OSU)
        val osu = LatLng(40.001, -83.017)

        // Check permissions and get the location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // Use the user's GPS location
                    val userLocation = LatLng(location.latitude, location.longitude)
                    userLocationMarker = googleMap.addMarker(
                        MarkerOptions()
                            .position(userLocation)
                            .title("You are here")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    )

                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
                    googleMap.moveCamera(CameraUpdateFactory.zoomTo(1f))
                } else {
                    // Use the default OSU location if the location is unavailable
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(osu))
                    googleMap.moveCamera(CameraUpdateFactory.zoomTo(1f))
                }
            }
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            // Default to OSU location while waiting for permission
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(osu))
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(1f))
        }

        // Observe the LiveData from the ViewModel
        vacationViewModel.allVacations.observe(this, Observer { vacations ->
            // Update the RecyclerView with the new list of vacations
            vacations.forEach{
                    vacation ->
                if(vacation.latitude != null && vacation.longitude != null) {
                    val marker =googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(vacation.latitude, vacation.longitude))
                            .title(vacation.title)
                    )
                    marker?.tag = vacation.id

                }
            }
        })


        googleMap.setOnMarkerClickListener { marker ->
            if (marker == userLocationMarker) {
                Toast.makeText(this, "You are here", Toast.LENGTH_SHORT).show()
                // Ignore clicks on the user location marker
                return@setOnMarkerClickListener true
            }
            val intent = Intent(this, VacationDetailedActivity::class.java)
            intent.putExtra("VACATION_ID", marker.tag as? String)
            startActivity(intent)
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, reinitialize the map
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            } else {
                // Permission denied, use the default OSU location
                val osu = LatLng(40.001, -83.017)
                val googleMap = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync { googleMap ->
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(osu))
                    googleMap.moveCamera(CameraUpdateFactory.zoomTo(1f))
                }
            }
        }
    }
}