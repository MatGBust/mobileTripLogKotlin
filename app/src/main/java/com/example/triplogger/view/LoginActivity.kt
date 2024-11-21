package com.example.triplogger.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.triplogger.R
import com.example.triplogger.model.VacationRepository
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class LoginActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var repository: VacationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check and prompt for language selection
        promptForLanguageIfNeeded()
    }

    private fun promptForLanguageIfNeeded() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val selectedLanguage = sharedPreferences.getString("selected_language", null)

        if (selectedLanguage != null) {
            // Language is already selected, set the locale and proceed with UI setup
            updateLocale(selectedLanguage)
            setupUI()
        } else {
            // Prompt the user to select a language
            showLanguageSelectionDialog()
        }
    }

    private fun setupUI() {
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        repository = VacationRepository()

        val loginButton = findViewById<Button>(R.id.login_button)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)

        loginButton.setOnClickListener {
            val email = username.text.toString().trim()
            val pass = password.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                signInOrSignUp(email, pass)
            } else {
                Toast.makeText(this, getString(R.string.enter_credentials), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInOrSignUp(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onLoginSuccess()
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { createTask ->
                    if (createTask.isSuccessful) {
                        onLoginSuccess()
                    } else {
                        Toast.makeText(
                            this,
                            getString(R.string.auth_failed, createTask.exception?.message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun showLanguageSelectionDialog() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        // Show the language selection dialog
        val languages = arrayOf("English", "Español")
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.select_language))
            .setItems(languages) { _, which ->
                val locale = when (which) {
                    0 -> "en" // English
                    1 -> "es" // Spanish
                    else -> "en"
                }

                // Save the selected language in SharedPreferences
                sharedPreferences.edit().putString("selected_language", locale).apply()

                // Update the locale
                updateLocale(locale)

                // Recreate the activity to apply the new language and set up the UI
                recreate()
            }
            .setCancelable(false)
            .show()
    }

    private fun onLoginSuccess() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("is_logged_in", true).apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
