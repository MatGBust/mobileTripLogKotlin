package com.example.triplogger

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.triplogger.ui.theme.TripLoggerTheme

private const val TAG = "MainActivity"
class MainActivity : ComponentActivity() {
    private lateinit var ivMapViewIcon : ImageView
    private  lateinit var  ivListViewIcon : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ivMapViewIcon = findViewById(R.id.ivMapViewIcon)
        ivListViewIcon = findViewById(R.id.ivListIcon)
        ivMapViewIcon.setOnClickListener(object : OnClickListener {
            override fun onClick(p0: View?) {
                Log.i(TAG, "ivMapViewIcon clicked ")
            }
        })
        ivListViewIcon.setOnClickListener(object : OnClickListener {
            override fun onClick(p0: View?) {
                Log.i(TAG, "ivListViewIcon clicked ")
            }
        })


    }
}

