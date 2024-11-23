package com.example.triplogger.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.triplogger.R
import com.example.triplogger.model.Vacation

private const val TAG = "ListViewActivity"

class ListViewActivity : BaseActivity() {

    private lateinit var vacationAdapter: VacationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        // Locale update happens in BaseActivity
        Log.i(TAG, "onCreate entered")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)

        val recyclerView = findViewById<RecyclerView>(R.id.vacationRecyclerView)
        vacationAdapter = VacationAdapter(mutableListOf(), vacationViewModel)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = vacationAdapter

        // Observe paginated vacations
        vacationViewModel.paginatedVacations.observe(this, Observer { vacations ->
            vacationAdapter.vacationList.clear()
            vacationAdapter.vacationList.addAll(vacations)
            vacationAdapter.notifyDataSetChanged()
        })

        // Load the first page of vacations
        vacationViewModel.loadNextPage(6)

        // Add pagination via scroll listener
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

        // Add new vacation button
        val buttonAddVacation = findViewById<Button>(R.id.buttonAddVacation)
        buttonAddVacation.setOnClickListener {
            val intent = Intent(this, AddVacationActivity::class.java)
            startActivity(intent)
        }
    }
}
