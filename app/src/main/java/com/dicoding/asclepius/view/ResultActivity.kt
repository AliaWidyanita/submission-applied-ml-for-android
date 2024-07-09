package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.data.local.HistoryRepository
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.model.ViewModelFactory
import com.dicoding.asclepius.view.model.ResultViewModel
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: ResultViewModel
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var historyRepository = HistoryRepository(application)
        val application = ViewModelFactory.getInstance(historyRepository)
        viewModel = ViewModelProvider(this@ResultActivity, application).get(ResultViewModel::class.java)

        val title = getString(R.string.result)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
        }
        classify(imageUri)
    }

    private fun classify(imageUri: Uri) {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@ResultActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(results: List<Classifications>?) {
                    runOnUiThread {
                        results?.let { it ->
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                val sortedCategories = it[0].categories.sortedByDescending { it?.score }
                                val label = sortedCategories[0].label
                                val score = (sortedCategories[0].score * 100).toInt()
                                val roundedScore = score.coerceAtMost(100)
                                val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI)).toString()
                                val history = History(image = imageUri, label = label, score = "$roundedScore%")
                                viewModel.insertResult(history)
                                val displayResult =
                                    sortedCategories.joinToString("\n") {
                                        "${it.label} " + NumberFormat.getPercentInstance()
                                            .format(it.score).trim()
                                    }
                                binding.resultText.text = displayResult
                            } else {
                                binding.resultText.text = ""
                            }
                        }
                    }
                }
            }
        )
        imageClassifierHelper.classifyStaticImage(imageUri)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}

