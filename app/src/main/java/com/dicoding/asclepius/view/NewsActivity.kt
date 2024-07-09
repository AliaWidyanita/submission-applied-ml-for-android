package com.dicoding.asclepius.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.view.adapter.NewsAdapter
import com.dicoding.asclepius.data.remote.ArticlesItem
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import com.dicoding.asclepius.view.model.NewsViewModel

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        val title = getString(R.string.berita)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this)
        binding.rvNews.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvNews.addItemDecoration(itemDecoration)

        viewModel.listNews.observe(this){ listNews ->
            listNews?.let {
                val adapter = NewsAdapter()
                adapter.submitList(listNews)
                binding.rvNews.adapter = adapter
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