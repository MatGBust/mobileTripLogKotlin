package com.example.triplogger.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.triplogger.R
import com.example.triplogger.model.Vacation
import com.example.vacationlogger.viewModel.VacationViewModel
import androidx.lifecycle.Observer

private const val TAG = "ListViewActivity"

class ListViewActivity : BaseActivity() {

    private lateinit var vacationAdapter: VacationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate entered")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)

        // Initialize the adapter with an empty list
        val recyclerView = findViewById<RecyclerView>(R.id.vacationRecyclerView)
        vacationAdapter = VacationAdapter(mutableListOf(), vacationViewModel) // Pass ViewModel to the adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = vacationAdapter

        // Observe the LiveData from the ViewModel
        vacationViewModel.allVacations.observe(this, Observer { vacations ->
            // Update the RecyclerView with the new list of vacations
            vacations?.let {
                vacationAdapter.vacationList.clear() // Clear the existing list
                vacationAdapter.vacationList.addAll(it) // Add all the new items
                vacationAdapter.notifyDataSetChanged()
            }
        })

        // Handle add new vacation
        val buttonAddVacation = findViewById<Button>(R.id.buttonAddVacation)
        buttonAddVacation.setOnClickListener {
            val intent = Intent(this, AddVacationActivity::class.java)
            startActivity(intent)
        }
    }
}