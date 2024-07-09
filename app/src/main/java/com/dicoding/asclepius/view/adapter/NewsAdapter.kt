package com.dicoding.asclepius.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.remote.ArticlesItem
import com.dicoding.asclepius.databinding.NewsItemBinding

class NewsAdapter: ListAdapter<ArticlesItem, NewsAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: ArticlesItem) {
            binding.tvTitle.text = news.title
            binding.tvDescription.text = news.description
            Glide.with(itemView.context)
                .load(news.urlToImage)
                .into(binding.ivNews)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}