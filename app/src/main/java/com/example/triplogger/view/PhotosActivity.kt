// PhotosActivity.kt
package com.example.triplogger.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.triplogger.R
import com.example.triplogger.model.Vacation
import com.example.vacationlogger.viewModel.VacationViewModel
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import android.util.Log

class PhotosActivity : AppCompatActivity() {

    private val vacationViewModel: VacationViewModel by viewModels()
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var vacationId: String
    private lateinit var storage: FirebaseStorage

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    uploadPhoto(uri)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)

        vacationId = intent.getStringExtra("VACATION_ID") ?: return
        storage = FirebaseStorage.getInstance() // Initialize Firebase Storage

        val recyclerView = findViewById<RecyclerView>(R.id.photosRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observe vacation photos
        vacationViewModel.getVacationById(vacationId).observe(this) { vacation ->
            if (vacation != null) {
                photoAdapter = PhotoAdapter(vacation.photos.toMutableList()) { photoUrl ->
                    deletePhoto(photoUrl)
                }
                recyclerView.adapter = photoAdapter
            }
        }

        // Set up "Add Photo" button to check permissions before opening the gallery
        findViewById<Button>(R.id.addPhotoButton).setOnClickListener {
            checkAndRequestPermissions()
        }
    }

    // Check and request storage permissions
    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 and above - request MANAGE_EXTERNAL_STORAGE permission
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, Uri.parse("package:$packageName"))
                startActivity(intent)
            } else {
                openGallery()
            }
        } else {
            // For Android 10 and below - request READ_EXTERNAL_STORAGE permission
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                openGallery()
            }
        }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(this, "Permission denied. Please enable storage permission in Settings.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        selectImageLauncher.launch(intent)
    }

    private fun uploadPhoto(imageUri: Uri) {
        val fileName = "photos/${UUID.randomUUID()}.jpg"
        val photoRef = storage.reference.child(fileName)

        Log.d("PhotosActivity", "Starting upload for file at path: $fileName")

        photoRef.putFile(imageUri)
            .addOnSuccessListener {
                Log.d("PhotosActivity", "Photo upload succeeded.")
                photoRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    Log.d("PhotosActivity", "Download URL: $downloadUrl")
                    savePhotoUrl(downloadUrl.toString())
                    Toast.makeText(this, "Photo added", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { exception ->
                    Log.e("PhotosActivity", "Failed to get download URL: ${exception.message}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("PhotosActivity", "Photo upload failed: ${exception.message}")
                Toast.makeText(this, "Failed to upload photo", Toast.LENGTH_SHORT).show()
            }
    }



    private fun savePhotoUrl(photoUrl: String) {
        vacationViewModel.getVacationById(vacationId).observe(this) { vacation ->
            vacation?.let {
                val updatedPhotos = it.photos.toMutableList().apply { add(photoUrl) }
                val updatedVacation = it.copy(photos = updatedPhotos)
                vacationViewModel.updateVacation(vacationId, updatedVacation)
                Log.d("PhotosActivity", "Photo URL added to vacation: $photoUrl")
            } ?: Log.e("PhotosActivity", "Failed to retrieve vacation for ID: $vacationId")
        }
    }

    private fun deletePhoto(photoUrl: String) {
        vacationViewModel.getVacationById(vacationId).observe(this) { vacation ->
            vacation?.let {
                val updatedPhotos = it.photos.toMutableList().apply { remove(photoUrl) }
                val updatedVacation = it.copy(photos = updatedPhotos)
                vacationViewModel.updateVacation(vacationId, updatedVacation)
                Toast.makeText(this, "Photo deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
