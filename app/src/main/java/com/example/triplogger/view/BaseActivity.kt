package com.example.triplogger.view

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vacationlogger.viewModel.VacationViewModel
import java.util.*

open class BaseActivity : AppCompatActivity() {

    open lateinit var vacationViewModel: VacationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // Update locale before calling super.onCreate
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val selectedLanguage = sharedPreferences.getString("selected_language", "en") ?: "en"
        updateLocale(selectedLanguage)
        super.onCreate(savedInstanceState)

        // Initialize the ViewModel
        vacationViewModel = ViewModelProvider(this).get(VacationViewModel::class.java)

        // Observe network errors in BaseActivity
        vacationViewModel.networkError.observe(this) { errorMessage ->
            errorMessage?.let {
                // Show the No Connection dialog or a Toast when there's a network issue
                showNoConnectionDialog()
            }
        }
    }

    public fun updateLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun showNoConnectionDialog() {
        AlertDialog.Builder(this)
            .setTitle("No Internet Connection")
            .setMessage("Please enable Wi-Fi or mobile data to use this app.")
            .setPositiveButton("Settings") { _, _ ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            .setNegativeButton("Exit") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }
}

