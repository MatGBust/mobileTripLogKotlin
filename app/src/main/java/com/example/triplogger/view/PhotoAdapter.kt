// PhotoAdapter.kt
package com.example.triplogger.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.triplogger.R
import com.bumptech.glide.Glide

class PhotoAdapter(
    private val photoList: MutableList<String>,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photoUrl = photoList[position]
        Glide.with(holder.itemView.context).load(photoUrl).into(holder.photoImageView)

        holder.deleteButton.setOnClickListener {
            onDeleteClick(photoUrl)
            photoList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = photoList.size
}
