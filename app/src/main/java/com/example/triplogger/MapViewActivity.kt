package com.example.triplogger

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


private const val TAG = "MapViewActivity"
class MapViewActivity : AppCompatActivity(), OnMapReadyCallback  {
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

        // Set the map coordinates to Kyoto Japan.
        val osu = LatLng(40.001, -83.017)

        // Set the map type to Hybrid.
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)

        // Add a marker on the map coordinates.
        googleMap.addMarker(
            MarkerOptions()
                .position(osu)
                .title("Ohio State University")
        )

        // Move the camera to the map coordinates and zoom in closer.
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(osu))


    }
}