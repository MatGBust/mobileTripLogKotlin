package com.example.triplogger.view

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.triplogger.R
import com.example.triplogger.model.Vacation
import com.example.vacationlogger.viewModel.VacationViewModel

private const val TAG = "VacationAdapter"

class VacationAdapter(val vacationList: MutableList<Vacation>,
                      private val vacationViewModel: VacationViewModel
) : RecyclerView.Adapter<VacationAdapter.VacationViewHolder>() {


    class VacationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)

        val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)
        val updateButton: Button = itemView.findViewById(R.id.buttonUpdate)
        val moreDetailsButton: Button = itemView.findViewById(R.id.buttonMoreDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.vacation_item, parent, false)
        return VacationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VacationViewHolder, position: Int) {
        val vacation = vacationList[position]
        holder.titleTextView.text = vacation.title
        holder.descriptionTextView.text = vacation.description
        holder.dateTextView.text = vacation.date
        holder.locationTextView.text = vacation.location
        // Set up delete button click listener
        holder.deleteButton.setOnClickListener {
            vacation.id?.let { id ->
                vacationViewModel.deleteVacation(id) // Pass the Firebase string ID to delete
                vacationList.removeAt(position)
                notifyItemRemoved(position)
            }
        }

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateVacationActivity::class.java).apply {
                putExtra("VACATION_ID", vacation.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.moreDetailsButton.setOnClickListener {
            Log.e(TAG, vacation.id.toString())
            val intent = Intent(holder.itemView.context, VacationDetailedActivity::class.java).apply {
                putExtra("VACATION_ID", vacation.id)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = vacationList.size
}
