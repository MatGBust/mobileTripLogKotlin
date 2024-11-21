package com.example.triplogger.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.triplogger.R
import com.example.triplogger.model.Vacation
import com.example.vacationlogger.viewModel.VacationViewModel
import com.example.triplogger.view.BaseActivity

class VacationAdapter(
    val vacationList: MutableList<Vacation>,
    private val vacationViewModel: VacationViewModel
) : RecyclerView.Adapter<VacationAdapter.VacationViewHolder>() {

    class VacationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)

        val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDelete)
        val updateButton: Button = itemView.findViewById(R.id.buttonUpdate)
        val moreDetailsButton: Button = itemView.findViewById(R.id.buttonMoreDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.vacation_item, parent, false)
        return VacationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VacationViewHolder, position: Int) {
        val vacation = vacationList[position]

        // Set vacation details
        holder.titleTextView.text = vacation.title
        holder.descriptionTextView.text = vacation.description
        holder.dateTextView.text = vacation.date
        holder.locationTextView.text = vacation.location

        // Ensure localized text for buttons by calling BaseActivity's locale setup
        val context = holder.itemView.context
        if (context is BaseActivity) {
            context.updateLocale(context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("selected_language", "en") ?: "en")
        }

        // Set localized text for buttons
        holder.updateButton.setText(R.string.update)
        holder.moreDetailsButton.setText(R.string.more_details)

        // Delete button listener
        holder.deleteButton.setOnClickListener {
            vacation.id?.let { id ->
                vacationViewModel.deleteVacation(id)
                vacationList.removeAt(position)
                notifyItemRemoved(position)
            }
        }

        // Update button listener
        holder.updateButton.setOnClickListener {
            val intent = Intent(context, UpdateVacationActivity::class.java).apply {
                putExtra("VACATION_ID", vacation.id)
            }
            context.startActivity(intent)
        }

        // More details button listener
        holder.moreDetailsButton.setOnClickListener {
            val intent = Intent(context, VacationDetailedActivity::class.java).apply {
                putExtra("VACATION_ID", vacation.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = vacationList.size
}
