package com.dicoding.asclepius.view.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.data.local.HistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyRepository: HistoryRepository): ViewModel() {
    private val mHistoryRepository = historyRepository.getAllHistory()
    val history: LiveData<List<History>> = mHistoryRepository

    init {
        getHistory()
    }

    private fun getHistory() {
        viewModelScope.launch {
            try {
                historyRepository.getAllHistory()
            } catch (e: Exception) {
                Log.d(TAG, "Gagal Mendapatkan Data: ${e.message}")
            }
        }
    }

    companion object {
        const val TAG = "HistoryViewModel"
    }
}