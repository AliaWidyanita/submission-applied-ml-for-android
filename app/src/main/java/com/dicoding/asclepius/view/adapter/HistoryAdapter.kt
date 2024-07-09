package com.dicoding.asclepius.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.databinding.HistoryItemBinding
import com.dicoding.asclepius.view.ResultActivity
import com.dicoding.asclepius.view.model.ResultViewModel

class HistoryAdapter: ListAdapter<History, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private val binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            binding.tvLabel.text = history.label
            binding.tvScore.text = history.score
            Glide.with(itemView.context)
                .load(history.image)
                .into(binding.ivResult)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, ResultActivity::class.java)
            intentDetail.putExtra(ResultActivity.EXTRA_IMAGE_URI, history.image)
            intentDetail.putExtra(ResultViewModel.LABEL, history.label)
            intentDetail.putExtra(ResultViewModel.SCORE, history.score)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }
        }
    }
}