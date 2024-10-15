package com.example.triplogger.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.triplogger.R
import com.example.triplogger.model.Vacation

private const val TAG = "ListViewActivity"

class ListViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate entered")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)

        val vacationList = listOf(
            Vacation("Summer Beach Trip", "Hawaii", "June 2023"),
            Vacation("Ski Adventure", "Colorado", "January 2023"),
            Vacation("City Tour", "New York", "December 2022")
        )

        val recyclerView = findViewById<RecyclerView>(R.id.vacationRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = VacationAdapter(vacationList)
    }
}