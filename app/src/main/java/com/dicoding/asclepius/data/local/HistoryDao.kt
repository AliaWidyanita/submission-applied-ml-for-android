package com.dicoding.asclepius.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(history: History)

    @Query("SELECT * from History")
    fun getAllHistory(): LiveData<List<History>>
}