package com.dicoding.asclepius.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.yalantis.ucrop.UCrop

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                moveToResult(it)
            } ?: run {
                showToast(getString(R.string.empty_warning))
            }
        }
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        analyzeImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
            Log.d("Image URI", "showImage: $it")
        }
    }

    private val analyzeImage = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        if (uri != null) {
            val destinationUri = Uri.fromFile(createTempFile("cropped_image", ".jpg"))
            UCrop.of(uri, destinationUri)
                .withAspectRatio(16f, 16f)
                .withMaxResultSize(1000, 1000)
                .start(this@MainActivity)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun moveToResult(uri: Uri) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            currentImageUri = resultUri
            showImage()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            showToast(cropError.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.history -> {
                val move = Intent(this@MainActivity, HistoryActivity::class.java)
                startActivity(move)
                true
            }
            R.id.news -> {
                val move = Intent(this@MainActivity, NewsActivity::class.java)
                startActivity(move)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}