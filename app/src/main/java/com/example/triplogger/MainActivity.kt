package com.example.triplogger

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.triplogger.fragments.ViewIconFragment
import androidx.fragment.app.commit

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate entered")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        loadFragment(R.id.fragment_map_container, ViewIconFragment.newInstance(R.drawable.map, getString(R.string.map_view)))
        loadFragment(R.id.fragment_list_container, ViewIconFragment.newInstance(R.drawable.notepad,  getString(R.string.list_view)))
    }
    private fun loadFragment(containerId: Int, fragment: Fragment) {
        supportFragmentManager.commit {
            replace(containerId, fragment)
            addToBackStack(null) // May want to delete later so user can't return to previous fragment
        }
    }
}

