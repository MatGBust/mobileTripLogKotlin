package com.example.triplogger.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
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
import com.example.vacationlogger.viewModel.VacationViewModel

class PhotosActivity : BaseActivity() {

    private val vacationViewModel: VacationViewModel by viewModels()
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var vacationId: String

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    savePhotoUri(uri.toString()) // Save URI directly as a string
                    Toast.makeText(this, "Photo added", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No photo selected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)

        vacationId = intent.getStringExtra("VACATION_ID") ?: return

        val recyclerView = findViewById<RecyclerView>(R.id.photosRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observe vacation photos
        vacationViewModel.getVacationById(vacationId).observe(this) { vacation ->
            if (vacation != null) {
                photoAdapter = PhotoAdapter(vacation.photos.toMutableList()) { photoUri ->
                    deletePhoto(photoUri)
                }
                recyclerView.adapter = photoAdapter
            }
        }

        // Add Photo button to check permissions before opening the gallery
        findViewById<Button>(R.id.addPhotoButton).setOnClickListener {
            checkAndRequestPermissions()
        }
    }

    // Check and request storage permissions
    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, Uri.parse("package:$packageName"))
                startActivity(intent)
            } else {
                openGallery()
            }
        } else {
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
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            Toast.makeText(this, "Permission denied. Please enable storage permission in Settings.", Toast.LENGTH_LONG).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        selectImageLauncher.launch(intent)
    }

    private fun savePhotoUri(uriString: String) {
        vacationViewModel.getVacationById(vacationId).observe(this) { vacation ->
            vacation?.let {
                val updatedPhotos = it.photos.toMutableList().apply { add(uriString) }
                val updatedVacation = it.copy(photos = updatedPhotos)
                vacationViewModel.updateVacation(vacationId, updatedVacation)
                Log.d("PhotosActivity", "Local URI added to vacation: $uriString")

                // Update the adapter with the new photo and notify it
                photoAdapter.photoList.add(uriString)
                photoAdapter.notifyItemInserted(photoAdapter.photoList.size - 1) // Notify adapter
            } ?: Log.e("PhotosActivity", "Failed to retrieve vacation for ID: $vacationId")
        }
    }


    private fun deletePhoto(photoUri: String) {
        vacationViewModel.getVacationById(vacationId).observe(this) { vacation ->
            vacation?.let {
                val updatedPhotos = it.photos.toMutableList().apply { remove(photoUri) }
                val updatedVacation = it.copy(photos = updatedPhotos)
                vacationViewModel.updateVacation(vacationId, updatedVacation)
                Toast.makeText(this, "Photo deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}