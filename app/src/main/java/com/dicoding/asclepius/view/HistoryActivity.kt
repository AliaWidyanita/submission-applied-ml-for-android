package com.dicoding.asclepius.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.view.adapter.HistoryAdapter
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.data.local.HistoryRepository
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.view.model.ViewModelFactory
import com.dicoding.asclepius.view.model.HistoryViewModel

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: HistoryViewModel
    private val adapter = HistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var historyRepository = HistoryRepository(application)
        val application = ViewModelFactory.getInstance(historyRepository)
        viewModel = ViewModelProvider(this@HistoryActivity, application).get(HistoryViewModel::class.java)

        val title = getString(R.string.histori_analisis)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this)
        binding.rvHistory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHistory.addItemDecoration(itemDecoration)

        viewModel.history.observe(this){ history ->
            val items = arrayListOf<History>()
            if (history.isNotEmpty()){
                history.map { history ->
                    val item = History(image = history.image, label = history.label, score = history.score)
                    items.add(item)
                }
                adapter.submitList(items)
                binding.rvHistory.adapter = adapter
            } else {
                binding.rvHistory.visibility = View.GONE
                binding.tvEmptyMessage.visibility = View.VISIBLE
                binding.tvEmptyMessage.text = getString(R.string.empty_message)
            }
        }
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
}