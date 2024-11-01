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
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate entered")
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)


        if (!isLoggedIn || auth.currentUser == null) {
            navigateToLogin()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val logoutButton = findViewById<Button>(R.id.btnLogout)

        logoutButton.setOnClickListener {
            logout()
        }

        val mapIconView = ViewIconFragment.newInstance(R.drawable.map, getString(R.string.map_view))
        val listIconView =
            ViewIconFragment.newInstance(R.drawable.notepad, getString(R.string.list_view))
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

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun logout() {
        // Log out from Firebase and clear login flag
        auth.signOut()
        sharedPreferences.edit().putBoolean("is_logged_in", false).apply()

        // Navigate back to the login activity
        navigateToLogin()
    }
}

