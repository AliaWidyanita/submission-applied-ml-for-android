package com.dicoding.asclepius.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.data.local.HistoryRepository
import kotlinx.coroutines.launch

class ResultViewModel (private val historyRepository: HistoryRepository): ViewModel() {
    fun insertResult(history: History) {
        viewModelScope.launch {
            historyRepository.add(history)
        }
    }

    companion object {
        const val LABEL = "key_label"
        const val SCORE = "key_score"
    }
}