package com.dicoding.asclepius.data.local

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryRepository (application: Application) {
    private val mHistoryDao: HistoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = HistoryDatabase.getDatabase(application)
        mHistoryDao = db.HistoryDao()
    }

    fun add(history: History) {
        executorService.execute {
            mHistoryDao.add(history)
        }
    }

    fun getAllHistory(): LiveData<List<History>> = mHistoryDao.getAllHistory()
}