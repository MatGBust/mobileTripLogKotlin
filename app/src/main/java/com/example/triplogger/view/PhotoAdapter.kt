// PhotoAdapter.kt
package com.example.triplogger.view

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.triplogger.R
import com.bumptech.glide.Glide
import java.io.InputStream

class PhotoAdapter(
    val photoList: MutableList<String>,
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
        val photoUriString = photoList[position]
        val photoUri = Uri.parse(photoUriString)

        // Attempt to load the image using Glide
        Glide.with(holder.itemView.context)
            .load(photoUri)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .into(holder.photoImageView)

        // Fallback: If Glide fails to load; InputStream to decode Google Photos or other URIs
        holder.photoImageView.post {
            if (holder.photoImageView.drawable == null) { // Check if Glide failed
                try {
                    // Open InputStream for the URI and decode it as a Bitmap
                    val inputStream: InputStream? = holder.itemView.context.contentResolver.openInputStream(photoUri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()

                    // Set the decoded Bitmap to the ImageView as a fallback
                    holder.photoImageView.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("PhotoAdapter", "Failed to load image from URI: $photoUriString")
                    holder.photoImageView.setImageResource(R.drawable.error) // Set error drawable if all else fails
                }
            }
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(photoUriString)
            photoList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = photoList.size
}