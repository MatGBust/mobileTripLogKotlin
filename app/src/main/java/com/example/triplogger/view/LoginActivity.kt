package com.example.triplogger.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.triplogger.R
import com.google.firebase.auth.FirebaseAuth
import com.example.triplogger.utilities.NetworkUtils.isNetworkAvailable

class LoginActivity : BaseActivity() {

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("user_prefs", MODE_PRIVATE)
    }
    private lateinit var auth: FirebaseAuth
    private var languageDialog: AlertDialog? = null
    private var isRecreating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        promptForLanguageIfNeeded()
    }

    override fun onResume() {
        super.onResume()
        if (!isNetworkAvailable(this)) {
            showNoConnectionDialog()
        }
    }

    private fun promptForLanguageIfNeeded() {
        if (isRecreating) return

        val selectedLanguage = sharedPreferences.getString("selected_language", null)
        if (selectedLanguage != null) {
            updateLocale(selectedLanguage)
            setupUI()
        } else {
            showLanguageSelectionDialog()
        }
    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf("English", "Español")
        languageDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.select_language))
            .setItems(languages) { _, which ->
                val locale = if (which == 1) "es" else "en"
                sharedPreferences.edit().putString("selected_language", locale).apply()
                updateLocale(locale)
                isRecreating = true
                recreate()
            }
            .setCancelable(false)
            .show()
    }

    private fun setupUI() {
        if (!::auth.isInitialized) {
            auth = FirebaseAuth.getInstance()
        }
        setContentView(R.layout.activity_login)

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

    override fun onDestroy() {
        super.onDestroy()
        languageDialog?.dismiss()
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

    private fun onLoginSuccess() {
        sharedPreferences.edit().putBoolean("is_logged_in", true).apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
