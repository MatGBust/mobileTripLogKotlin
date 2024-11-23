package com.example.triplogger.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.triplogger.R
import com.example.triplogger.model.Vacation
import com.example.vacationlogger.viewModel.VacationViewModel

private const val TAG = "ListViewActivity"

class ListViewActivity : AppCompatActivity() {

    private lateinit var vacationAdapter: VacationAdapter
    private val vacationViewModel: VacationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate entered")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)

        val recyclerView = findViewById<RecyclerView>(R.id.vacationRecyclerView)
        vacationAdapter = VacationAdapter(mutableListOf(), vacationViewModel)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = vacationAdapter

        vacationViewModel.paginatedVacations.observe(this, Observer { vacations ->
            vacationAdapter.vacationList.clear()
            vacationAdapter.vacationList.addAll(vacations)
            vacationAdapter.notifyDataSetChanged()
        })

        vacationViewModel.loadNextPage(6) // Load the first page of 10 items

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (totalItemCount <= lastVisibleItem + 3) {
                    vacationViewModel.loadNextPage(6) // Load next page
                }
            }
        })

        val buttonAddVacation = findViewById<Button>(R.id.buttonAddVacation)
        buttonAddVacation.setOnClickListener {
            val intent = Intent(this, AddVacationActivity::class.java)
            startActivity(intent)
        }
    }
}
