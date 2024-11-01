package com.example.triplogger.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.triplogger.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.login_button)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)

        loginButton.setOnClickListener {
            val isAuthenticated = true

            if (isAuthenticated) {
                val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                sharedPreferences.edit().putBoolean("is_logged_in", true).apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
