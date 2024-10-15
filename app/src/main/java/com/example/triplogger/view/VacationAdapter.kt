package com.example.triplogger.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.triplogger.R
import com.example.triplogger.model.Vacation

class VacationAdapter(private val vacationList: List<Vacation>) : RecyclerView.Adapter<VacationAdapter.VacationViewHolder>() {

    class VacationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.vacation_item, parent, false)
        return VacationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VacationViewHolder, position: Int) {
        val vacation = vacationList[position]
        holder.titleTextView.text = vacation.title
        holder.locationTextView.text = vacation.location
        holder.dateTextView.text = vacation.date
    }

    override fun getItemCount() = vacationList.size
}
