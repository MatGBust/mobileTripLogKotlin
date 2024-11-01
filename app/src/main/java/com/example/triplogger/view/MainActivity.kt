package com.example.triplogger.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.triplogger.R
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate entered")
        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        if (!isLoggedIn) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val logoutButton = findViewById<Button>(R.id.btnLogout)

        logoutButton.setOnClickListener {
            sharedPreferences.edit().putBoolean("is_logged_in", false).apply()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val mapIconView =  ViewIconFragment.newInstance(R.drawable.map, getString(R.string.map_view))
        val listIconView = ViewIconFragment.newInstance(R.drawable.notepad,  getString(R.string.list_view))
        loadFragment(R.id.fragment_map_container, mapIconView)
        loadFragment(R.id.fragment_list_container, listIconView)
    }

    override fun onPause() {
        Log.i(TAG, "onPause entered")
        super.onPause()
    }

    override fun onResume() {
        Log.i(TAG, "onResume entered")
        super.onResume()
    }

    override fun onStop() {
        Log.i(TAG, "onStop entered")
        super.onStop()
    }

    private fun loadFragment(containerId: Int, fragment: Fragment) {
        supportFragmentManager.commit {
            replace(containerId, fragment)
        }
    }
}

