package com.example.triplogger

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "MapViewActivity"
class MapViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate entered")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_view)
    }
}